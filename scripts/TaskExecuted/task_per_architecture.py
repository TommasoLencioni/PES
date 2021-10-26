# importing the required module
import matplotlib.pyplot as plt
import csv
import os

import numpy
import numpy as np
from scipy.interpolate import make_interp_spline, BSpline, interpolate
from scipy.ndimage.filters import gaussian_filter1d


def main():
    seconds_cloud = []
    tasks_cloud = []
    seconds_edge = []
    tasks_edge = []
    num = 0
    f_csv = csv.reader(open('data/cloud.csv'), delimiter=",")
    # num = num + 1
    for i, line in enumerate(f_csv):
        # if not len(x)==0 and not x[len(x) - 1] == int(float(line[0])):
        seconds_cloud.append(int(float(line[0])/60))
        tasks_cloud.append(int(float(line[1]))+1)

    f_csv = csv.reader(open('data/edge.csv'), delimiter=",")
    # num = num + 1
    for i, line in enumerate(f_csv):
        # if not len(x)==0 and not x[len(x) - 1] == int(float(line[0])):
        seconds_edge.append(int(float(line[0])/60))
        tasks_edge.append(int(float(line[1]))+1)

    plt.plot(seconds_cloud, tasks_cloud, label='Cloud tasks over time')
    plt.plot(seconds_edge, tasks_edge, label='Edge tasks over time')

    # naming the x axis
    plt.xlabel('Simulation Time')
    # naming the y axis
    plt.ylabel('Task over time')

    plt.gcf().set_size_inches(12, 8)
    plt.legend()
    # giving a title to my graph
    plt.title('Tasks over time')
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
