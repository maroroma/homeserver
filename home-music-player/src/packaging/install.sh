sudo systemctl stop homemusicplayer.service
#rm /home/luna/SHARE/musicplayer/home-music-player-${project.version}.jar
cp -r home-music-player-${project.version}.jar /home/luna/SHARE/musicplayer
sudo cp homemusicplayer.service  /etc/systemd/system/homemusicplayer.service
sudo systemctl daemon-reload
sudo systemctl enable homemusicplayer.service
sudo systemctl start homemusicplayer.service
