# install deps

# apt install build-essential fakeroot devscripts debhelper maven

mvn install -Dassembly.skipAssembly=false

dpkg-buildpackage -A -rfakeroot -us -uc -tc -d

echo "=================================================="
echo ".deb file is now in parent folder"

