
# -*- encoding: utf-8 -*-

# load env.cfg

import logging
import configparser
from urllib import parse


config = configparser.SafeConfigParser()  # LATER load all env var here logger Level


class BaseConfig(object):

    CURRENT_VERSION = "/api/v1"

    TENCENT_EMAIL_SERVER_IP = "smtp.exmail.qq.com"
    TENCENT_EMAIL_SERVER_PORT = 456

    MARVIN_AUTHP = "******"
    MARVIN_EMAIL = "******"
    XARC_EMAIL = "******"

    TEST_EMAIL_PROJECT_ADMIN = "test.admin@xtalpi.com"
    TEST_EMAIL_PROJECT_OWNER = "test.owner@xtalpi.com"

    SOCKETIO_ADDRESS = "http://127.0.0.1:8080"

    BASIC_AUTH_PASSWORD = "admin"
    BASIC_AUTH_USERNAME = "admin"

    SSL_DISABLE = False
    SCHEDULER_TIMEZONE = "Asia/Shanghai"

    MAX_CONTENT_LENGTH = 10 * 1024 * 1024
    LOGIN_TOKEN_EXPIRATION = 24 * 60 * 60

    # PERMANENT_SESSION_LIFETIME = 5

    SQLALCHEMY_POOL_SIZE = 10
    SQLALCHEMY_POOL_TIMEOUT = 15
    SQLALCHEMY_MAX_OVERFLOW = 1000
    SQLALCHEMY_RECORD_QUERIES = True
    SQLALCHEMY_COMMIT_ON_TEARDOWN = True
    SQLALCHEMY_TRACK_MODIFICATIONS = False

    EMAIL_TEST_RECEIVER = []
    EMAIL_TEST_CC_LIST = []

    NITROGEN_TEST_AUTHP = "******"
    NITROGEN_TEST_BASEURL = "******"
    NITROGEN_PROD_BASEURL = "******"
    LOGS_FILE = "logs/log.log"


class DevConfig(BaseConfig):
    ENV = "DEV"
    DEBUG = True
    TESTING = True
    SECRET_KEY = "Dev Key"
    STAIC_URL_PATH = "/static/"
    TEMPLATE_FOLDER = "/data/template/"
    STATIC_FOLDER = "/data/static/"
    LOGGING_LEVEL = logging.DEBUG
    LOGS_FILE = "/data/logs/log.log"
    DATABASE_AUTH = parse.unquote_plus("******")
    SQLALCHEMY_DATABASE_URI = 'mysql+pymysql://root:%s@127.0.0.1:3306/******?charset=utf8' % DATABASE_AUTH
