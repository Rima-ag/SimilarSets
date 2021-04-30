cin = open('./bread basket.txt', 'r')
cout = open('./in', 'w')

current = "1"
for line in cin:
    line = line.rstrip("\n")
    if not line.startswith("Tr"):
        elem = [string for string in line.split(",") if string != "" ]
        if elem[0] == current:
            cout.write(elem[1] + "\n")
        else:
            cout.write("--\n"+elem[1] + "\n")
            current = elem[0]
cout.write("--")
cin.close()
cout.close()