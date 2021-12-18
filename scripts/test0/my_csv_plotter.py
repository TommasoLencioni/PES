# importing the required module
import matplotlib.pyplot as plt
import csv
import os

import numpy


def main():
    task_generated_nolead = [None]*4  # 7
    task_succ_exe_nolead = [None]*4   # 8
    tasks_failed_delay_nolead = [None]*4   # 10
    tasks_failed_mobility_nolead = [None]*4   # 12

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('NOLEADER/output/50_noleader'):
        num = 0
        for simulations_file in os.listdir('NOLEADER/output/50_noleader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('NOLEADER/output/50_noleader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_nolead[0] = int(numpy.average(task_generated))
    task_succ_exe_nolead[0] = int(numpy.average(task_succ_exe))/int(numpy.average(task_generated))*100
    tasks_failed_delay_nolead[0] = int(numpy.average(tasks_failed_delay))/int(numpy.average(task_generated))*100
    tasks_failed_mobility_nolead[0] = int(numpy.average(tasks_failed_mobility))/int(numpy.average(task_generated))*100

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('NOLEADER/output/100_noleader'):
        num = 0
        for simulations_file in os.listdir('NOLEADER/output/100_noleader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('NOLEADER/output/100_noleader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_nolead[1] = int(numpy.average(task_generated))
    task_succ_exe_nolead[1] = int(numpy.average(task_succ_exe))/int(numpy.average(task_generated))*100
    tasks_failed_delay_nolead[1] = int(numpy.average(tasks_failed_delay))/int(numpy.average(task_generated))*100
    tasks_failed_mobility_nolead[1] = int(numpy.average(tasks_failed_mobility))/int(numpy.average(task_generated))*100

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('NOLEADER/output/150_noleader'):
        num = 0
        for simulations_file in os.listdir('NOLEADER/output/150_noleader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('NOLEADER/output/150_noleader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_nolead[2] = int(numpy.average(task_generated))
    task_succ_exe_nolead[2] = int(numpy.average(task_succ_exe))/int(numpy.average(task_generated))*100
    tasks_failed_delay_nolead[2] = int(numpy.average(tasks_failed_delay))/int(numpy.average(task_generated))*100
    tasks_failed_mobility_nolead[2] = int(numpy.average(tasks_failed_mobility))/int(numpy.average(task_generated))*100

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('NOLEADER/output/200_noleader'):
        num = 0
        for simulations_file in os.listdir('NOLEADER/output/200_noleader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('NOLEADER/output/200_noleader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_nolead[3] = int(numpy.average(task_generated))
    task_succ_exe_nolead[3] = int(numpy.average(task_succ_exe)) / int(numpy.average(task_generated)) * 100
    tasks_failed_delay_nolead[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(task_generated)) * 100
    tasks_failed_mobility_nolead[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(task_generated)) * 100

    '''LEADER'''

    task_generated_lead = [None] * 5  # 8
    task_succ_exe_lead = [None] * 5  # 10
    tasks_failed_delay_lead = [None] * 5  # 12
    tasks_failed_mobility_lead = [None] * 5  # 14

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('LEADER/output/50_leader'):
        num = 0
        for simulations_file in os.listdir('LEADER/output/50_leader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('LEADER/output/50_leader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[0] = int(numpy.average(task_generated))
    task_succ_exe_lead[0] = int(numpy.average(task_succ_exe)) / int(numpy.average(task_generated)) * 100
    tasks_failed_delay_lead[0] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(task_generated)) * 100
    tasks_failed_mobility_lead[0] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(task_generated)) * 100

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('LEADER/output/100_leader'):
        num = 0
        for simulations_file in os.listdir('LEADER/output/100_leader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('LEADER/output/100_leader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[1] = int(numpy.average(task_generated))
    task_succ_exe_lead[1] = int(numpy.average(task_succ_exe)) / int(numpy.average(task_generated)) * 100
    tasks_failed_delay_lead[1] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(task_generated)) * 100
    tasks_failed_mobility_lead[1] = int(numpy.average(tasks_failed_mobility)) / int(numpy.average(task_generated)) * 100

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('LEADER/output/125_leader'):
        num = 0
        for simulations_file in os.listdir('LEADER/output/125_leader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('LEADER/output/125_leader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[2] = int(numpy.average(task_generated))
    task_succ_exe_lead[2] = int(numpy.average(task_succ_exe)) / int(numpy.average(task_generated)) * 100
    tasks_failed_delay_lead[2] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(task_generated)) * 100
    tasks_failed_mobility_lead[2] = int(numpy.average(tasks_failed_mobility)) / int(numpy.average(task_generated)) * 100

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('LEADER/output/150_leader'):
        for simulations_file in os.listdir('LEADER/output/150_leader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('LEADER/output/150_leader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[3] = int(numpy.average(task_generated))
    task_succ_exe_lead[3] = int(numpy.average(task_succ_exe)) / int(numpy.average(task_generated)) * 100
    tasks_failed_delay_lead[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(task_generated)) * 100
    tasks_failed_mobility_lead[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(task_generated)) * 100

    task_generated = list()  # 8
    task_succ_exe = list()  # 10
    tasks_failed_delay = list()  # 12
    tasks_failed_mobility = list()  # 14
    for simulation_folder in os.listdir('LEADER/output/200_leader'):
        for simulations_file in os.listdir('LEADER/output/200_leader/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('LEADER/output/200_leader/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                task_generated.append(int(float(elem)))
                            if j == 8:
                                task_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[4] = int(numpy.average(task_generated))
    task_succ_exe_lead[4] = int(numpy.average(task_succ_exe)) / int(numpy.average(task_generated)) * 100
    tasks_failed_delay_lead[4] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(task_generated)) * 100
    tasks_failed_mobility_lead[4] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(task_generated)) * 100

    print(tasks_failed_mobility_nolead)
    edge_num = [50, 100, 150, 200]
    edge_num_lead = [50, 100, 125, 150, 200]
    # plt.plot(edge_num, task_generated_nolead, label="Tasks generated")
    plt.plot(edge_num, task_succ_exe_nolead, label="% Tasks successfully excuted NOLEAD")
    plt.plot(edge_num_lead, task_succ_exe_lead, label="% Tasks successfully excuted LEAD")
    plt.plot(edge_num, tasks_failed_delay_nolead, label="% Tasks failed due to delay NOLEAD")
    plt.plot(edge_num_lead, tasks_failed_delay_lead, label="% Tasks failed due to delay LEAD")
    plt.plot(edge_num, tasks_failed_mobility_nolead, label="% Tasks failed due to mobility NOLEAD")
    plt.plot(edge_num_lead, tasks_failed_mobility_lead, label="% Tasks failed due to mobility LEAD")
    # naming the x axis
    plt.xlabel('Number of edge devices')
    # naming the y axis
    plt.ylabel('Task generated')

    plt.gcf().set_size_inches(12, 6)

    # giving a title to my graph
    plt.title('Overview')
    plt.legend()
    # function to show the plot
    plt.show()




if __name__ == "__main__":
    main()
