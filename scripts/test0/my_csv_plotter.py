# importing the required module
import matplotlib.pyplot as plt
import csv
import os

import numpy
from scipy.interpolate import make_interp_spline, BSpline
from scipy.ndimage.filters import gaussian_filter1d


def main():
    simulation_time = []
    generated_tasks = []
    failed_tasks = []
    tasks_failed_due_to_latency = []
    tasks_failed_due_to_mobility = []
    tasks_executed_on_cloud = []
    tasks_failed_on_cloud = []
    tasks_executed_on_edge = []
    tasks_failed_on_edge = []
    wan_utilization = []
    #stats = []
    #stats = [list() for i in range(10)]
    import numpy as np
    #stats = np.empty(10, dtype=list)
    stats = [None] * 10
    for i in range(10):
        stats[i] = list()
    #for filename in os.listdir('50'):
    f_csv = csv.reader(open('./results/50/Emy.csv'), delimiter=",")
    for i, line in enumerate(f_csv):
        for i in range(10):
            #print(line[i])
            stats[i].append(int(float(line[i])))
        '''
        #print(stats)
        '''

    plt.plot(stats[0], stats[2], label="Failed tasks")
    plt.plot(stats[0], stats[8], label="Task failed on edge")
    # naming the x axis
    plt.xlabel('Simulation Time in minutes')
    # naming the y axis
    plt.ylabel('Mbits/s')

    plt.gcf().set_size_inches(12, 6)

    # giving a title to my graph
    plt.title('Task failure')
    plt.legend()
    # function to show the plot
    plt.show()


if __name__ == "__main__":
    main()
