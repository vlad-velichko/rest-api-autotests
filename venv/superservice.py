import hashlib
import random
import uuid

import bottle
import sys
import time
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import scoped_session, sessionmaker

_token = None
base = declarative_base()
engine = create_engine('sqlite:///main.db')


class Uploads(base):
    __tablename__ = 'uploads'

    id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(String(128), nullable=False)
    payload_md5 = Column(String(32), nullable=False)


def create_db():
    base.metadata.create_all(bind=engine)


@bottle.route('/ping/', method='GET')
def ping():
    return 'OK'


@bottle.route('/authorize/', method='POST')
def authorize():
    username = bottle.request.forms.get('username', '')
    password = bottle.request.forms.get('password', '')
    if username == 'supertest' and password == 'superpassword':
        global _token
        _token = {'token': str(uuid.uuid4()), 'valid_until': time.time() + 60}
        return {'token': _token['token']}
    bottle.abort(403, 'Forbidden')


@bottle.route('/api/save_data/', method='POST')
def save_data():
    auth = bottle.request.headers.get('Authorization', '').split('Bearer', 1)
    if len(auth) < 2:
        bottle.abort(403, 'Forbidden')
    if not _token:
        bottle.abort(403, 'Forbidden')
    if auth[1].strip() != _token['token']:
        bottle.abort(403, 'Forbidden')
    if _token['valid_until'] < time.time():
        bottle.abort(403, 'Forbidden')
    if bottle.request.json:
        payload = bottle.request.json.get('payload')
    else:
        payload = bottle.request.forms.get('payload')
    if not payload:
        bottle.abort(400, 'Bad request')
    session = scoped_session(sessionmaker(bind=engine))
    new_data = Uploads(
        user_id='supertest',
        payload_md5=hashlib.md5(payload.encode('utf-8')).hexdigest(),
    )
    session.add(new_data)
    session.commit()
    if random.randint(1, 100) < 50:
        return {'status': 'ERROR', 'error': 'I dont like this payload'}
    return {'id': new_data.id, 'status': 'OK'}


if __name__ == '__main__':
    if 'migrate' in sys.argv:
        create_db()
    if 'run' in sys.argv:
        bottle.run(port=5000)
