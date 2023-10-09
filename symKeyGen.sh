keyFile=$1
openssl enc -aes-256-cbc -k secret -P -md sha1 -nosalt  > $keyFile
keyLine=$(head -n 1 $keyFile)
key=${keyLine#"key="}
echo $key > $keyFile
#rm $keyFile
