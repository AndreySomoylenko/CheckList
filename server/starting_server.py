import socket, logging


# ––––––––––––––– get env –––––––––––––––

PASSWORD = ''
IP = '127.0.0.1'
MYSQL_IP = ''
PORT = 8000
LOGFILL = './logging.log'
TELBOT_TOKEN = ''
ADMINS = 0
HDRS = 'HTTP/1.1 200 OK\r\n Content-Type: text/html charset=utf-8'

# –––––––––––––– log setup ––––––––––––––

logging.basicConfig(
    level=logging.INFO,
    filename=LOGFILL,
    format="%(asctime)s [%(levelname)s] %(message)s"
)


def get(table, **kwargs):
    pass
    

def add():
    pass


def dell():
    pass


def start_server():

    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind((IP, PORT))
    server.listen(6)
    logging.info('START SERVER')
    while True:
        try:
            client_socket, address = server.accept()
            data = client_socket.recv(1024).decode('utf-8')
            return_data = {"GH": "NONE"}
            client_socket.send(HDRS.encode('utf-8') + str(return_data).encode('utf-8'))
            client_socket.shutdown(socket.SHUT_WR)
            logging.info(data.format().split()[1])
        except Exception as e:
            server.close()
            logging.error(e.__str__())
            return None


if __name__ == "__main__":
    start_server()
