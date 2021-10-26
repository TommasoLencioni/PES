# importing the required module
import matplotlib.pyplot as plt
import csv
import os

import numpy


def main():
    generated_tasks = 0
    tasks_succ_exe = 0
    tasks_not_exe = 0
    tasks_failed_delay = 0
    tasks_failed_mobility = 0
    tasks_exec_cloud = 0
    tasks_exec_edge = 0
    num = 0
    xaxes=[]
    yaxes=[]
    zaxes=[]
    for filename in os.listdir('iteration'):
        f_csv = csv.reader(open('iteration/' + filename), delimiter=",")
        x = []
        y = []
        z = []

        for i, line in enumerate(f_csv):
            if not i==0:
                for j, elem in enumerate(line):
                    if j == 2:
                        x.append(int(elem))
                    if j == 7:
                        succ= int(elem)
                    if j == 10:
                        fail_del=int(elem)
                    if j == 12:
                        fail_mob = int(elem)
                y.append(int(fail_del/succ*100))
                z.append(int(fail_mob/succ*100))
        # plotting the points
        #plt.plot(x, y)
        xaxes.append(x)
        yaxes.append(y)
        zaxes.append(z)
    mean_x = numpy.mean(xaxes, axis=0)
    mean_y = numpy.mean(yaxes, axis=0)
    mean_z = numpy.mean(zaxes, axis=0)
    plt.plot(mean_x, mean_y, color='black', label="Failed due to delay")
    plt.plot(mean_x, mean_z, color='blue', label="Failed due to mobility")
    # naming the x axis
    plt.xlabel('x - Number of devices')
    # naming the y axis
    plt.ylabel('y - Delay failure rate')

    # giving a title to my graph
    plt.title('Delay failure rate per number of devices')
    plt.gcf().set_size_inches(12, 8)
    # print legend
    plt.legend()
    # function to show the plot
    plt.show()
    '''
    # x axis values
    x = [1,2,3]
    # corresponding y axis values
    y = [2,4,1]

    # plotting the points
    plt.plot(x, y)

    # naming the x axis
    plt.xlabel('x - axis')
    # naming the y axis
    plt.ylabel('y - axis')

    # giving a title to my graph
    plt.title('My first graph!')

    # function to show the plot
    plt.show()
    '''


if __name__ == "__main__":
    main()
