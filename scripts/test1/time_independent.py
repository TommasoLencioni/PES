# importing the required module
import matplotlib.pyplot as plt
import csv
import os


def variance(data):
    # Number of observations
    n = len(data)
    # Mean of the data
    mean = sum(data) / n
    # Square deviations
    deviations = [(x - mean) ** 2 for x in data]
    # Variance
    v = sum(deviations) / n
    return v


def main():
    generated_tasks = 0
    tasks_succ_exe = 0
    tasks_not_exe = 0
    tasks_failed_delay = 0
    tasks_failed_mobility = 0
    tasks_exec_cloud = 0
    tasks_exec_edge = 0
    num = 0

    minutes = []
    delay = []
    mobility = []
    edge = []
    cloud = []
    for filename in os.listdir('./csvs'):
        if filename is not None:
            f_csv = csv.reader(open('./csvs/' + filename), delimiter=",")
        num = num + 1
        for i, line in enumerate(f_csv):
            for j, elem in enumerate(line):
                # if j == 7 and i == 1:
                #    generated_tasks = generated_tasks + int(elem)
                if j == 8 and i == 1:
                    tasks_succ_exe = int(elem)
                # if j == 9 and i == 1:
                #    tasks_not_exe = tasks_not_exe + int(elem)
                if j == 10 and i == 1:
                    tasks_failed_delay = int(elem)
                if j == 12 and i == 1:
                    tasks_failed_mobility = int(elem)
                if j == 15 and i == 1:
                    tasks_exec_cloud = int(elem)
                if j == 17 and i == 1:
                    tasks_exec_edge = int(elem)
        minutes.append(filename.replace(".csv", ""))
        delay.append(tasks_failed_delay / tasks_succ_exe * 100)
        mobility.append(tasks_failed_mobility / tasks_succ_exe * 100)
        cloud.append(tasks_exec_cloud / tasks_succ_exe * 100)
        edge.append(tasks_exec_edge / tasks_succ_exe * 100)

    # plotting the points
    plt.plot(minutes, delay, label="Failed due to delay")
    plt.plot(minutes, mobility, label="Failed due to mobility")

    # naming the x axis
    plt.xlabel('x - Minutes of simulation')
    # naming the y axis
    plt.ylabel('y - Failure rate')

    # giving a title to my graph
    plt.title('Failure rate and simulation time')
    plt.gcf().set_size_inches(12, 8)
    plt.legend()
    # function to show the plot
    plt.show()

    cloud_variances = []
    i=0
    while i<len(cloud):
        cloud_variances.append(variance(cloud[i:]))
        i += 1

    edge_variances = []
    i = 0
    while i<len(edge):
        edge_variances.append(variance(edge[i:]))
        i += 1

    # plotting the points
    plt.plot(minutes, cloud, label="Tasks executed by cloud")
    plt.plot(minutes, edge, label="Tasks executed by edge")
    # naming the x axis
    plt.xlabel('x - Minutes of simulation')
    # naming the y axis
    plt.ylabel('y - Execution rate')

    # giving a title to my graph
    plt.title('Executor and simulation time')
    plt.gcf().set_size_inches(12, 8)
    plt.legend()
    # function to show the plot
    plt.show()

    plt.plot(minutes, cloud_variances, label="Variances in tasks executed by edge and cloud")
    plt.title('Variances in tasks executed by edge and cloud')
    plt.show()


if __name__ == "__main__":
    main()
