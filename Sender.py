import socket, sys, time
from time import sleep
import  pifacedigitalio as pfio
import time

pfd = pfio.PiFaceDigital()
pfio.init()

headlessPiSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
port = 5050
server_address = ('10.0.0.52', port)
headlessPiSocket.bind(server_address)
headlessPiSocket.setblocking(0)


serverPi = ('10.0.0.51', port)


while(True):

        if(pfd.input_pins[6].value == 1 or pfd.input_pins[7].value == 1):

                                print ("OMG FLAME DETECTED")

                                pfio.digital_write(0,1)

                                notification = "fire notification"

                                headlessPiSocket.sendto(notification.encode('utf-8'), serverPi)

                                while (True):
                                        try:
                                                buf, address = headlessPiSocket.recvfrom(port)
                                                print ("RESPONSE RECEIVED")
                                                sleep(20)
                                                break

                                        except socket.error:
                                                headlessPiSocket.sendto(notification.encode('utf-8'), serverPi)

        else:
                print ("No smoke or flame detected")
                sleep(0.5)


~                                                                                                                                                                                                                  
~                                                                                                                                                                                                                  
~                                                                                                                                                                                                                  
~                                                                                                
