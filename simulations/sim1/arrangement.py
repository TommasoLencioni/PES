# importing the required module
import matplotlib.pyplot as plt
import csv
import os

import numpy
import matplotlib.ticker as mticker


def main():
    '''
        Task successfully executed, task failed due to mobility, task executed by cloud and edge
        All for number of users
    '''

    '''GENERAL NOLEAD COUNTERS'''
    tasks_generated_nolead = [None] * 5  # 7
    tasks_succ_exe_nolead = [None] * 5 # 8
    tasks_failed_latency_nolead = [None] * 5  # 10
    tasks_failed_mobility_nolead = [None] * 5  # 12
    tasks_exe_cloud_nolead = [None] * 5  # 14
    tasks_succ_exe_cloud_nolead = [None] * 5  # 15
    tasks_exe_edge_nolead = [None] * 5  # 16
    tasks_succ_exe_edge_nolead = [None] * 5  # 17

    '''50 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/50_users/NOLEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/50_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/50_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))


    tasks_generated_nolead[0] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[0] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[0] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[0] = int(numpy.average(tasks_failed_mobility)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_nolead[0] = int(numpy.average(tasks_exe_cloud))/ int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[0] = int(numpy.average(tasks_succ_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_edge_nolead[0] = int(numpy.average(tasks_exe_edge))/int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[0] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100

    '''100 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/100_users/NOLEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/100_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/100_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_nolead[1] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[1] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[1] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[1] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_nolead[1] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[1] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_nolead[1] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[1] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''150 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/150_users/NOLEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/150_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/150_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_nolead[2] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[2] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[2] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[2] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_nolead[2] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[2] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_nolead[2] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[2] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''200 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/200_users/NOLEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/200_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/200_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_nolead[3] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[3] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_nolead[3] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[3] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_nolead[3] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[3] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''300 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/300_users/NOLEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/300_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/300_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_nolead[4] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[4] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[4] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[4] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_nolead[4] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[4] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_nolead[4] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[4] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

########################################################################################################################
    '''GENERAL LEAD COUNTERS'''
    tasks_generated_lead = [None] * 5  # 7
    tasks_succ_exe_lead = [None] * 5  # 8
    tasks_failed_delay_lead = [None] * 5  # 10
    tasks_failed_mobility_lead = [None] * 5  # 12
    tasks_exe_cloud_lead = [None] * 5  # 14
    tasks_succ_exe_cloud_lead = [None] * 5  # 15
    tasks_exe_edge_lead = [None] * 5  # 16
    tasks_succ_exe_edge_lead = [None] * 5  # 17

    '''50 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/50_users/LEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/50_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/50_users/LEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[0] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[0] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[0] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[0] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_lead[0] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[0] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_lead[0] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[0] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''100 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/100_users/LEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/100_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/100_users/LEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[1] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[1] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[1] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[1] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_lead[1] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[1] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_lead[1] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[1] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100


    '''150 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/150_users/LEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/150_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/150_users/LEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[2] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[2] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[2] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[2] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_lead[2] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[2] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_lead[2] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[2] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''200 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/200_users/LEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/200_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/200_users/LEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[3] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[3] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_lead[3] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[3] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_lead[3] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[3] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''300 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/300_users/LEAD'):
        for simulations_file in os.listdir('quadrant_arrangement/300_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/300_users/LEAD/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[4] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[4] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[4] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[4] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_lead[4] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[4] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_lead[4] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[4] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

#############################################################################################################################
    '''GENERAL NEIGHBOR COUNTERS'''
    tasks_generated_neigh = [None] * 5  # 7
    tasks_succ_exe_neigh = [None] * 5  # 8
    tasks_failed_latency_neigh = [None] * 5  # 10
    tasks_failed_mobility_neigh = [None] * 5  # 12
    tasks_exe_cloud_neigh = [None] * 5  # 14
    tasks_succ_exe_cloud_neigh = [None] * 5  # 15
    tasks_exe_edge_neigh = [None] * 5  # 17
    tasks_succ_exe_edge_neigh = [None] * 5  # 17

    '''50 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/50_users/NEIGH'):
        for simulations_file in os.listdir('quadrant_arrangement/50_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/50_users/NEIGH/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[0] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[0] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[0] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[0] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_neigh[0] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[0] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_neigh[0] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[0] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''100 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/100_users/NEIGH'):
        for simulations_file in os.listdir('quadrant_arrangement/100_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/100_users/NEIGH/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[1] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[1] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[1] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[1] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_neigh[1] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[1] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_neigh[1] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[1] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''150 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/150_users/NEIGH'):
        for simulations_file in os.listdir('quadrant_arrangement/150_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/150_users/NEIGH/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[2] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[2] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[2] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[2] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_neigh[2] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[2] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_neigh[2] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[2] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''200 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/200_users/NEIGH'):
        for simulations_file in os.listdir('quadrant_arrangement/200_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/200_users/NEIGH/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[3] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[3] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_neigh[3] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[3] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_neigh[3] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[3] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''300 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_exe_cloud = list()  # 14
    tasks_succ_exe_cloud = list()  # 15
    tasks_exe_edge = list()  # 16
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('quadrant_arrangement/300_users/NEIGH'):
        for simulations_file in os.listdir('quadrant_arrangement/300_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('quadrant_arrangement/300_users/NEIGH/' + simulation_folder + '/' + simulations_file),
                                   delimiter=",")
                for i, line in enumerate(f_csv):
                    if i == 1:
                        for j, elem in enumerate(line):
                            if j == 7:
                                tasks_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))
                            if j == 14:
                                tasks_exe_cloud.append(int(float(elem)))
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 16:
                                tasks_exe_edge.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[4] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[4] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[4] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[4] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_exe_edge_neigh[4] = int(numpy.average(tasks_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[4] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_exe_cloud_neigh[4] = int(numpy.average(tasks_exe_cloud)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[4] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    users_num = [50, 100, 150, 200, 300]

    '''Task successfully executed'''
    plt.plot(users_num, tasks_succ_exe_nolead, label="Non-federated", marker='.')
    plt.plot(users_num, tasks_succ_exe_lead, label="Federated", marker='^')
    plt.plot(users_num, tasks_succ_exe_neigh, label="Neighbor", marker='o')
    plt.xlabel('Number of users')
    plt.ylabel('% of successfully executed tasks over generated')
    plt.gcf().set_size_inches(12, 6)
    #plt.title('Successfully executed tasks and number of users')
    plt.gca().xaxis.set_major_formatter(mticker.FormatStrFormatter('%d'))
    plt.gca().yaxis.set_major_formatter(mticker.PercentFormatter())
    plt.legend()
    plt.show()

    '''Tasks failed due to latency'''
    plt.plot(users_num, tasks_failed_latency_nolead, label="Non-federated", marker='.')
    plt.plot(users_num, tasks_failed_delay_lead, label="Federated", marker='^')
    plt.plot(users_num, tasks_failed_latency_neigh, label="Neighbor", marker='o')
    plt.xlabel('Number of users')
    plt.ylabel('% of failed tasks due to total latency')
    plt.gcf().set_size_inches(12, 6)
    #plt.title('Failed tasks due to latency and orchestration deployment')
    plt.gca().xaxis.set_major_formatter(mticker.FormatStrFormatter('%d'))
    plt.gca().yaxis.set_major_formatter(mticker.PercentFormatter())
    plt.legend()
    plt.show()

    '''Tasks failed due to user mobility'''
    plt.plot(users_num, tasks_failed_mobility_nolead, label="Non-federated", marker='.')
    plt.plot(users_num, tasks_failed_mobility_lead, label="Federated", marker='^')
    plt.plot(users_num, tasks_failed_mobility_neigh, label="Neighbor", marker='o')
    plt.xlabel('Number of users')
    plt.ylabel('% of failed tasks due to users\' mobility')
    plt.gcf().set_size_inches(12, 6)
    #plt.title('Failed tasks due to users\' mobility and orchestration deployment')
    plt.gca().xaxis.set_major_formatter(mticker.FormatStrFormatter('%d'))
    plt.gca().yaxis.set_major_formatter(mticker.PercentFormatter())
    plt.legend()
    plt.show()

    '''Tasks successfully executed on cloud'''
    plt.plot(users_num, tasks_succ_exe_cloud_nolead, label="Non-federated", marker='.')
    plt.plot(users_num, tasks_succ_exe_cloud_lead, label="Federated", marker='^')
    plt.plot(users_num, tasks_succ_exe_cloud_neigh, label="Neighbor", marker='o')
    plt.xlabel('Number of users')
    plt.ylabel('% of tasks successfully executed on cloud')
    plt.gcf().set_size_inches(12, 6)
    #plt.title('Tasks executed on cloud and orchestration deployment')
    plt.gca().xaxis.set_major_formatter(mticker.FormatStrFormatter('%d'))
    plt.gca().yaxis.set_major_formatter(mticker.PercentFormatter())
    plt.legend()
    plt.show()

    '''Tasks successfully executed on edge'''
    plt.plot(users_num, tasks_succ_exe_edge_nolead, label="Non-federated", marker='.')
    plt.plot(users_num, tasks_succ_exe_edge_lead, label="Federated", marker='^')
    plt.plot(users_num, tasks_succ_exe_edge_neigh, label="Neighbor", marker='o')
    plt.xlabel('Number of users')
    plt.ylabel('% of tasks successfully executed on edge')
    plt.gcf().set_size_inches(12, 6)
    #plt.title('Tasks executed on edge and orchestration deployment')
    plt.gca().xaxis.set_major_formatter(mticker.FormatStrFormatter('%d'))
    plt.gca().yaxis.set_major_formatter(mticker.PercentFormatter())
    plt.legend()
    plt.show()

    '''Tasks executed on edge'''
    plt.plot(users_num, tasks_exe_edge_nolead, label="Non-federated", marker='.')
    plt.plot(users_num, tasks_exe_edge_lead, label="Federated", marker='^')
    plt.plot(users_num, tasks_exe_edge_neigh, label="Neighbor", marker='o')
    plt.xlabel('Number of users')
    plt.ylabel('% of tasks executed on edge')
    plt.gcf().set_size_inches(12, 6)
    # plt.title('Tasks executed on edge and orchestration deployment')
    plt.gca().xaxis.set_major_formatter(mticker.FormatStrFormatter('%d'))
    plt.gca().yaxis.set_major_formatter(mticker.PercentFormatter())
    plt.legend()
    plt.show()

    '''Tasks executed on cloud'''
    plt.plot(users_num, tasks_exe_cloud_nolead, label="Non-federated", marker='.')
    plt.plot(users_num, tasks_exe_cloud_lead, label="Federated", marker='^')
    plt.plot(users_num, tasks_exe_cloud_neigh, label="Neighbor", marker='o')
    plt.xlabel('Number of users')
    plt.ylabel('% of tasks executed on cloud')
    plt.gcf().set_size_inches(12, 6)
    # plt.title('Tasks executed on cloud and orchestration deployment')
    plt.gca().xaxis.set_major_formatter(mticker.FormatStrFormatter('%d'))
    plt.gca().yaxis.set_major_formatter(mticker.PercentFormatter())
    plt.legend()
    plt.show()

if __name__ == "__main__":
    main()
