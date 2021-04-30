cin = open('./orders.csv', 'r')
cout = open('./inOrders', 'w')

current = "1"
for line in cin:
    line = line.rstrip("\n")
    if not line.startswith("order"):
        elem = [string for string in line.split(",") if string != "" ]
        if elem[0] == current:
            cout.write(elem[1] + "\n")
        else:
            cout.write("--\n"+elem[1] + "\n")
            current = elem[0]
cout.write("--")
cin.close()
cout.close()