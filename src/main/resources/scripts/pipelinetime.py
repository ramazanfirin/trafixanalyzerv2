import sys
import logging
import time

#logging.basicConfig(filename='/home/ramazan/Desktop/aitest/new_logs/new_log.log', filemode='w', level=logging.INFO)
#logger = logging.getLogger(__name__)

print(sys.argv)
#logger.info("started")
print('started')

if len(sys.argv) > 1:
    #logger.info("got arguments")
    #logger.info(f"{sys.argv}")
    print('sys.argv')
else:
    #logger.info("noo")
    #logger.info(f"{sys.argv}")
    print('noo')

time.sleep(15)
print('finished')

