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
    tasks_generated_nolead = [None] * 4  # 7
    tasks_succ_exe_nolead = [None] * 4  # 8
    tasks_failed_latency_nolead = [None] * 4  # 10
    tasks_failed_mobility_nolead = [None] * 4  # 12
    tasks_succ_exe_cloud_nolead = [None] * 4  # 15
    tasks_succ_exe_edge_nolead = [None] * 4  # 17

    '''50 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/50_users/NOLEAD'):
        for simulations_file in os.listdir('moving/50_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/50_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))


    tasks_generated_nolead[0] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[0] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[0] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[0] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[0] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[0] = int(numpy.average(tasks_succ_exe_cloud)) / int(numpy.average(tasks_generated)) * 100

    '''100 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/100_users/NOLEAD'):
        for simulations_file in os.listdir('moving/100_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/100_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_nolead[1] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[1] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[1] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[1] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[1] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[1] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''150 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/150_users/NOLEAD'):
        for simulations_file in os.listdir('moving/150_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/150_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_nolead[2] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[2] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[2] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[2] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[2] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[2] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''200 USERS NOLEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/200_users/NOLEAD'):
        for simulations_file in os.listdir('moving/200_users/NOLEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/200_users/NOLEAD/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_nolead[3] = int(numpy.average(tasks_generated))
    tasks_succ_exe_nolead[3] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_nolead[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_nolead[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_nolead[3] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_nolead[3] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

########################################################################################################################
    '''GENERAL LEAD COUNTERS'''
    tasks_generated_lead = [None] * 4  # 7
    tasks_succ_exe_lead = [None] * 4  # 8
    tasks_failed_delay_lead = [None] * 4  # 10
    tasks_failed_mobility_lead = [None] * 4  # 12
    tasks_succ_exe_cloud_lead = [None] * 4  # 15
    tasks_succ_exe_edge_lead = [None] * 4  # 17

    '''50 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/50_users/LEAD'):
        for simulations_file in os.listdir('moving/50_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/50_users/LEAD/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[0] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[0] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[0] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[0] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[0] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[0] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''100 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/100_users/LEAD'):
        for simulations_file in os.listdir('moving/100_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/100_users/LEAD/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[1] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[1] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[1] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[1] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[1] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[1] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100


    '''150 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/150_users/LEAD'):
        for simulations_file in os.listdir('moving/150_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/150_users/LEAD/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[2] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[2] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[2] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[2] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[2] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[2] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''200 USERS LEAD COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/200_users/LEAD'):
        for simulations_file in os.listdir('moving/200_users/LEAD/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/200_users/LEAD/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_lead[3] = int(numpy.average(tasks_generated))
    tasks_succ_exe_lead[3] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_delay_lead[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_lead[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_lead[3] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_lead[3] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100


    '''GENERAL NEIGHBOR COUNTERS'''
    tasks_generated_neigh = [None] * 4  # 7
    tasks_succ_exe_neigh = [None] * 4  # 8
    tasks_failed_latency_neigh = [None] * 4  # 10
    tasks_failed_mobility_neigh = [None] * 4  # 12
    tasks_succ_exe_cloud_neigh = [None] * 4  # 15
    tasks_succ_exe_edge_neigh = [None] * 4  # 17

    '''50 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/50_users/NEIGH'):
        for simulations_file in os.listdir('moving/50_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/50_users/NEIGH/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[0] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[0] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[0] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[0] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[0] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[0] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''100 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/100_users/NEIGH'):
        for simulations_file in os.listdir('moving/100_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/100_users/NEIGH/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[1] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[1] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[1] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[1] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[1] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[1] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''150 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/150_users/NEIGH'):
        for simulations_file in os.listdir('moving/150_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/150_users/NEIGH/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[2] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[2] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[2] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[2] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[2] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[2] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100

    '''200 USERS NEIGH COUNTERS'''
    tasks_generated = list()  # 7
    tasks_succ_exe = list()  # 8
    tasks_failed_delay = list()  # 10
    tasks_failed_mobility = list()  # 12
    tasks_succ_exe_cloud = list()  # 15
    tasks_succ_exe_edge = list()  # 17
    for simulation_folder in os.listdir('moving/200_users/NEIGH'):
        for simulations_file in os.listdir('moving/200_users/NEIGH/' + simulation_folder):
            if simulations_file == 'Parallel_simulation_1.csv':
                f_csv = csv.reader(open('moving/200_users/NEIGH/' + simulation_folder + '/' + simulations_file),
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
                            if j == 15:
                                tasks_succ_exe_cloud.append(int(float(elem)))
                            if j == 17:
                                tasks_succ_exe_edge.append(int(float(elem)))

    tasks_generated_neigh[3] = int(numpy.average(tasks_generated))
    tasks_succ_exe_neigh[3] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_latency_neigh[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tasks_generated)) * 100
    tasks_failed_mobility_neigh[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tasks_generated)) * 100
    tasks_succ_exe_edge_neigh[3] = int(numpy.average(tasks_succ_exe_edge)) / int(numpy.average(tasks_generated)) * 100
    tasks_succ_exe_cloud_neigh[3] = int(numpy.average(tasks_succ_exe_cloud)) / int(
        numpy.average(tasks_generated)) * 100



    users_num = [50, 100, 150, 200]

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

    '''Tasks executed on cloud'''
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

    '''Tasks executed on edge'''
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

    '''LEADER

    task_generated_lead = [None] * 5  # 8
    task_succ_exe_lead = [None] * 5  # 10
    tasks_failed_delay_lead = [None] * 5  # 12
    tasks_failed_mobility_lead = [None] * 5  # 14

    tmp_task_generated = list()  # 8
    tasks_succ_exe = list()  # 10
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
                                tmp_task_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[0] = int(numpy.average(tmp_task_generated))
    task_succ_exe_lead[0] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_delay_lead[0] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_mobility_lead[0] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tmp_task_generated)) * 100

    tmp_task_generated = list()  # 8
    tasks_succ_exe = list()  # 10
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
                                tmp_task_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[1] = int(numpy.average(tmp_task_generated))
    task_succ_exe_lead[1] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_delay_lead[1] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_mobility_lead[1] = int(numpy.average(tasks_failed_mobility)) / int(numpy.average(tmp_task_generated)) * 100

    tmp_task_generated = list()  # 8
    tasks_succ_exe = list()  # 10
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
                                tmp_task_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[2] = int(numpy.average(tmp_task_generated))
    task_succ_exe_lead[2] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_delay_lead[2] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_mobility_lead[2] = int(numpy.average(tasks_failed_mobility)) / int(numpy.average(tmp_task_generated)) * 100

    tmp_task_generated = list()  # 8
    tasks_succ_exe = list()  # 10
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
                                tmp_task_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[3] = int(numpy.average(tmp_task_generated))
    task_succ_exe_lead[3] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_delay_lead[3] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_mobility_lead[3] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tmp_task_generated)) * 100

    tmp_task_generated = list()  # 8
    tasks_succ_exe = list()  # 10
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
                                tmp_task_generated.append(int(float(elem)))
                            if j == 8:
                                tasks_succ_exe.append(int(float(elem)))
                            if j == 10:
                                tasks_failed_delay.append(int(float(elem)))
                            if j == 12:
                                tasks_failed_mobility.append(int(float(elem)))

    task_generated_lead[4] = int(numpy.average(tmp_task_generated))
    task_succ_exe_lead[4] = int(numpy.average(tasks_succ_exe)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_delay_lead[4] = int(numpy.average(tasks_failed_delay)) / int(numpy.average(tmp_task_generated)) * 100
    tasks_failed_mobility_lead[4] = int(numpy.average(tasks_failed_mobility)) / int(
        numpy.average(tmp_task_generated)) * 100

    print(tasks_failed_mobility_nolead)
    users_num = [50, 100, 150, 200]
    edge_num_lead = [50, 100, 125, 150, 200]
    # plt.plot(users_num, task_generated_nolead, label="Tasks generated")
    plt.plot(users_num, tasks_succ_exe_nolead, label="% Tasks successfully excuted NOLEAD")
    plt.plot(edge_num_lead, task_succ_exe_lead, label="% Tasks successfully excuted LEAD")
    plt.plot(users_num, tasks_failed_latency_nolead, label="% Tasks failed due to delay NOLEAD")
    plt.plot(edge_num_lead, tasks_failed_delay_lead, label="% Tasks failed due to delay LEAD")
    plt.plot(users_num, tasks_failed_mobility_nolead, label="% Tasks failed due to mobility NOLEAD")
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
'''



if __name__ == "__main__":
    main()
