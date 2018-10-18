# coding=utf-8

import sys
import os

"""
	argv[1] - Ejecución modo experimento: exp / noexp [OK] /// -
	argv[2] - Ejecución modo random: random / norandom [OK] /// 0 (OBG)
	argv[3] - Algoritmo de Resolución: hc / sa /// 1 (OBG)
	argv[4] - Tipo solución inicial: t / p /// 2 (OBG)
	argv[5] - Número de clientes. /// 3
	argv[6], argv[7], argv[8] - Proporción de tipos de clientes /// 4 5 6
	argv[9] - Proporcion de clientes garantizados /// 7
	argv[10], argv[11], argv[12] - Proporción de tipos de centrales /// 8 9 10
	argv[13] - Semilla de randoms /// 11
"""

if __name__ == '__main__':

	namefile = "energIA.jar"

	if str(sys.argv[1]) == "exp":
		for i in range(0, 10):
			if str(sys.argv[2]) == "random":
				os.system("java -jar %s %s %s %s >> experimento.txt" % (namefile, "r", sys.argv[3], sys.argv[4]))
			else:
				os.system("java -jar %s %s %s %s %s %s %s %s %s %s %s %s %s >> experimento.txt" % (namefile, 'nr', sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6], sys.argv[7], sys.argv[8], sys.argv[9], sys.argv[10], sys.argv[11], sys.argv[11], sys.argv[13]))
	else:
		if str(sys.argv[2]) == "random":
			os.system("java -jar %s %s %s %s" % (namefile, "r", sys.argv[3], sys.argv[4]))
		else:
			os.system("java -jar %s %s %s %s %s %s %s %s %s %s %s %s %s" % (namefile, 'nr', sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6], sys.argv[7], sys.argv[8], sys.argv[9], sys.argv[10], sys.argv[11], sys.argv[11], sys.argv[13]))
