# Peer-to-Peer-File-Transferring
Implementation of a file distribution system using Java programming language, Computer Network final course project, Fall 2018 <br/>

In this project, a system can distribute or recieve a file. In distribution mode, when the system recieves a request for a specific file, it sends the file with UDP protocol, and in recieve mode, it can request broadcasting for the file in order to be sent to all the hubs in the network. If one of the hubs gets the request and has the file, it will send it. <br/>
When the system is distributing a file, it should have a specific port. 
