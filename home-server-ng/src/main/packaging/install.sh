sudo systemctl stop homeserver.service
mkdir /media/disk02/SHARE/homeserver-${project.version}
cp -r * /media/disk02/SHARE/homeserver-${project.version}
sudo cp /media/disk02/SHARE/homeserver-${project.version}/homeserver.service  /etc/systemd/system/homeserver.service
sudo systemctl daemon-reload
sudo systemctl start homeserver.service
