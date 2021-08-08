import sys
import logging

logging.basicConfig(filename='/home/ramazan/Desktop/aitest/new_logs/new_log.log', filemode='w', level=logging.INFO)
logger = logging.getLogger(__name__)

print(sys.argv)

if len(sys.argv) > 1:
    logger.info("got arguments")
    #logger.info(f"{sys.argv}")
else:
    logger.info("noo")
    #logger.info(f"{sys.argv}")
