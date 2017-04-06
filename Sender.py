import socket, sys, time
from time import sleep
import  pifacedigitalio as pfio
import time

pfd = pfio.PiFaceDigital()
pfio.init()


#Creating the socket to send the notification over. We will also receive a response from the server on this socket.
headlessPiSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
port = 5050
server_address = ('10.0.0.52', port)
headlessPiSocket.bind(server_address)
#Socket is set to be non-blocking, this is because we want to continously send notifications to server until we get a response. 
#A response means the server received the packet. 
headlessPiSocket.setblocking(0) 
#Server information
serverPi = ('10.0.0.51', port)
#If input pins have this value it means they detected a fire
flameFound = 1

debugging = True

while(True):

        if(pfd.input_pins[6].value == flameFound or pfd.input_pins[7].value == flameFound):

                                if(debugging):
					print ("OMG FLAME DETECTED")

                                if(debugging):
					pfio.digital_write(0,1)
				#Generate and send the notification now that flame is detected
                                notification = "fire notification"

                                headlessPiSocket.sendto(notification.encode('utf-8'), serverPi)

                                while (True):
					#Now that notification has been seen, attempt to receive response. If none is received,
					#an exception is thrown, caught, and another notification gets sent. Once notification 
					#is received, Sender goes to sleep, then begins polling input pins again for a fire.	
                                        try:
                                                buf, address = headlessPiSocket.recvfrom(port)
                                                if(debugging):
							print ("RESPONSE RECEIVED")
                                                sleep(20)
                                                break

                                        except socket.error:
                                                headlessPiSocket.sendto(notification.encode('utf-8'), serverPi)

        else:
                if(debugging):
			print ("No smoke or flame detected")
                sleep(0.5)

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
