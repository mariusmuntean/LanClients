1. A **LAN sampling tool** for the raspberry pi. It periodically collects information about clients in the LAN. This is done by the so-called LanClientsScan module. Collection is done robustly(recovers from restarts and crashes) and with an adjustable time period.

2. A **simple Vaadin web app** to show the collected data. This web app is hosted on the same raspberry pi in a Tomcat instance.