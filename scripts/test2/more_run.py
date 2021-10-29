# importing the required module
import matplotlib.pyplot as plt
import csv
import os

import numpy


def main():
    '''
    This script gets more run and averages the values in my.csv
    Needs to be completed
    '''

    simulation_times = dict()
    failed_tasks_all = dict()
    stats50leader = [[list() for i in range(11)]]
    for simulation_folder in os.listdir('LEADER/output/50_leader'):
        num = 0
        for simulations_file in os.listdir('LEADER/output/50_leader/' + simulation_folder):
            if simulations_file == '1my.csv':
                simulation_time = dict()
                generated_tasks = dict()
                failed_tasks = dict()
                tasks_failed_due_to_latency = dict()
                tasks_failed_due_to_mobility = dict()
                tasks_executed_on_cloud = dict()
                tasks_failed_on_cloud = dict()
                tasks_executed_on_edge = dict()
                tasks_failed_on_edge = dict()
                wan_utilization = dict()
                offload_to_leader = dict()
                f_csv = csv.reader(open('LEADER/output/50_leader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for line in f_csv:
                    stat = [None] * 11
                    for j in range(11):
                        stat[j] = (int(float(line[j])))
                    simulation_time[stat[0]] = stat[0]
                    generated_tasks[stat[0]] = stat[1]
                    failed_tasks[stat[0]] = stat[2]
                    tasks_failed_due_to_latency[stat[0]] = stat[3]
                    tasks_failed_due_to_mobility[stat[0]] = stat[4]
                    tasks_executed_on_cloud[stat[0]] = stat[5]
                    tasks_failed_on_cloud[stat[0]] = stat[6]
                    tasks_executed_on_edge[stat[0]] = stat[7]
                    tasks_failed_on_edge[stat[0]] = stat[8]
                    wan_utilization[stat[0]] = stat[9]
                    offload_to_leader[stat[0]] = stat[10]
                for k, v in simulation_time.items():
                    if simulation_times.get(k) is None:
                        simulation_times[k] = list()
                    simulation_times[k].append(v)
                for k, v in failed_tasks.items():
                    if failed_tasks_all.get(k) is None:
                        failed_tasks_all[k] = list()
                    failed_tasks_all[k].append(v)
    for k, v in failed_tasks_all.items():
        failed_tasks_all[k] = int(numpy.average(v))
    simtime = list()
    failed = list()

    for k, v in failed_tasks_all.items():
        simtime.append(k)
        failed.append(v)
    simtime.sort()
    failed.sort()
    print(failed_tasks_all)
    # mean_x = numpy.mean(failed_tasks0, axis=0)
    plt.plot(simtime, failed, label="Failed tasks")
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
