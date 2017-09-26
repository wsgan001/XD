javac -classpath . data_format/asthma_pefr_reader.java
java -classpath . data_format/asthma_pefr_reader "AsthmaData\氣喘病患資料庫\pefr_2005All.csv" "AsthmaData\氣象局資料\2005HT.txt" "AsthmaData\環保署空污資料\2005air.txt" "AsthmaData"
cmd