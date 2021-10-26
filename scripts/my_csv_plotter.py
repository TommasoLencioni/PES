# importing the required module
import matplotlib.pyplot as plt
import csv
import os

import numpy
from scipy.interpolate import make_interp_spline, BSpline
from scipy.ndimage.filters import gaussian_filter1d


def main():
    xaxes=[]
    yaxes=[]
    num = 0
    for filename in os.listdir('netusage'):
        x=[]
        y=[]
        if filename is not None:
            f_csv = csv.reader(open('netusage/' + filename), delimiter=",")
            # num = num + 1
            for i, line in enumerate(f_csv):
                # if not len(x)==0 and not x[len(x) - 1] == int(float(line[0])):
                x.append(int(float(line[0]))/60)
                y.append(int(float(line[1])))
            # plotting the points
            #plt.plot(x, y)
            xaxes.append(x)
            yaxes.append(y)

    mean_x = numpy.mean(xaxes, axis=0)
    mean_y = numpy.mean(yaxes, axis=0)
    plt.plot(mean_x, mean_y)

    # naming the x axis
    plt.xlabel('Simulation Time in minutes')
    # naming the y axis
    plt.ylabel('Mbits/s')

    plt.gcf().set_size_inches(12, 6)

    # giving a title to my graph
    plt.title('WAN Usage over time')
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
