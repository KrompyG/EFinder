import os

# paste here path to your key.json
os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = "/home/maxim/Documents/efinder/server/key.json"

class Config(object):
    SECRET_KEY = os.environ.get('SECRET_KEY') or 'strong-secret-key-here'
    ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])
    UPLOAD_FOLDER = '../img/'
    JSON_AS_ASCII = False
    