#-------------imports----------------------------------
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from sqlalchemy import create_engine, Column, String, Integer, Float, TIMESTAMP, BOOLEAN
from sqlalchemy.orm import declarative_base
from sqlalchemy.orm import sessionmaker
import psycopg2
from datetime import datetime
import logstash
import logging
from timeit import default_timer as timer
from threading import Thread

# ------------ElkLogConfig---------------------------
start = timer()
host = 'logstash'

logger = logging.getLogger(__name__)
syslog = logging.StreamHandler()
formatter = logging.Formatter('%(asctime)s %(act-role)s : %(message)s')
syslog.setFormatter(formatter)
logger.setLevel(logging.INFO)
logger.addHandler(syslog)
logger.addHandler(logstash.LogstashHandler(host,5001,version=1))

# ------------SeleniumConfig---------------------------
options = webdriver.ChromeOptions()
options.add_experimental_option("detach", True)
options.add_argument("--headless=new")
options.add_argument("--headless")
options.add_argument("--no-sandbox")
options.add_argument("--disable-dev-shm-usage")
#addblocker
# options.add_extension(r'/home/aliak/PycharmProjects/ExchangeApp/addblocker.crx')

chrome_prefs = {}
options.experimental_options["prefs"] = chrome_prefs
chrome_prefs["profile.default_content_settings"] = {"images": 2}

s = Service(r'/home/aliak/PycharmProjects/PythonWebScrapingApp')
# ------------SqlalchemyConfig---------------------------
Base = declarative_base()

conn_string = "host='postgres' dbname='currencies'\
user='aliak' password='aliak'"

conn = psycopg2.connect(conn_string)
class Currency(Base):
    __tablename__ = "currencies"

    id = Column("id", Integer, primary_key=True, autoincrement='auto')
    bank = Column("bank", String)
    currency = Column("currency", String)
    buy = Column("buy", Float)
    sell = Column("sell", Float)
    date = Column("date", TIMESTAMP)
    deleted = Column("deleted", BOOLEAN)

    def __init__(self, bank, currency, buy, sell, date, deleted):

        self.bank = bank
        self.currency = currency
        self.buy = buy
        self.sell = sell
        self.date = date
        self.deleted = deleted
    def __repr__(self):
        return f"({self.id}) {self.bank} {self.currency} {self.buy} {self.sell} {self.date} {self.deleted}"

engine = create_engine("postgresql+psycopg2://aliak:aliak@postgres:5432/currencies", echo=True)
Base.metadata.create_all(bind=engine)

Session = sessionmaker(bind=engine)
session = Session()

#---------------WebScraping-------------------------
banks = ["Akbank","Denizbank","Halkbank","Vakıfbank"]
currencies =["USD","EUR","GBP"]

#--------------------Gold-------------------
driver = webdriver.Chrome(options=options, service=s)
url = 'https://altin.doviz.com/gram-altin'
driver.get(url)
drivers = [driver]

gold_bank_name = WebDriverWait(driver, 10).until(EC.presence_of_all_elements_located((By.XPATH, '//a[@data-ga-event="asset_detail_other_sources_click"]')))
gold_buy_sell = WebDriverWait(driver, 10).until(EC.presence_of_all_elements_located((By.XPATH, '//a[@data-ga-event="asset_detail_other_sources_click"]/../../td[@class="text-bold"]')))

gold_list = []
for i in range(0,len(gold_bank_name)):

    if any(gold_bank_name[i].text in banks for bank in banks):
        gold_list.append(gold_buy_sell[i * 4].text.replace(".",""))
        gold_list.append(gold_buy_sell[(i * 4) + 1].text.replace(".",""))

#--------------Currency-------------------------
bank_hrefs = ["https://kur.doviz.com/akbank","https://kur.doviz.com/denizbank","https://kur.doviz.com/halkbank","https://kur.doviz.com/vakifbank"]
def currency_scrape(bank_href,bank,driver):

    driver.get(bank_href)
    currency_buy_sell = WebDriverWait(driver, 10).until(
        EC.presence_of_all_elements_located((By.XPATH, '//tr[@role="row"]/td[@class = "text-bold"]')))

    for j in range(0, len(currencies)):

        buy = float(currency_buy_sell[j*2].text.replace(',','.'))

        sell = float(currency_buy_sell[(j*2)+1].text.replace(',','.'))

        data = Currency(bank,currencies[j],buy,sell,time,False)
        session.add(data)

time = datetime.now()
threads = []
for i in range(0,len(bank_hrefs)):
    driver = webdriver.Chrome(options=options, service=s)
    drivers.append(driver)
    t = Thread(target=currency_scrape, args=(bank_hrefs[i],banks[i],driver))
    t.start()
    threads.append(t)


    gold_buy = float(gold_list[i*2].replace(',','.'))
    gold_sell = float(gold_list[(i*2)+1].replace(',','.'))
    data = Currency(banks[i],"GOLD",gold_buy,gold_sell,time,False)
    session.add(data)

for t in threads:
    t.join()

session.commit()
print("Data has benn scraped")
end = timer()

duration = end - start
formatted_duration = formatted_num = "{:.2f}".format(duration)
string_duration = str(formatted_duration) + "s"

for bank in banks:

    extra = {
        'act-role': 'python-log',
        'duration': string_duration,
        'bank-name': bank
    }

    logger = logging.LoggerAdapter(logger, extra)
    logger.info("Data has been scraped from " + bank)
    logger = logging.getLogger(__name__)

print("time :",time)
for driver in drivers:
    driver.close()

