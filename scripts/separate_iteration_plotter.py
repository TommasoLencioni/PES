# importing the required module
import matplotlib.pyplot as plt
import csv
import os


def main():
    generated_tasks = 0
    tasks_succ_exe = 0
    tasks_not_exe = 0
    tasks_failed_delay = 0
    tasks_failed_mobility = 0
    tasks_exec_cloud = 0
    tasks_exec_edge = 0
    num = 0
    for filename in os.listdir('results'):
        f_csv = csv.reader(open('results/' + filename), delimiter=",")
        num = num + 1
        for i, line in enumerate(f_csv):
            for j, elem in enumerate(line):
                if j == 7 and i == 1:
                    generated_tasks = generated_tasks + int(elem)
                if j == 8 and i == 1:
                    tasks_succ_exe = tasks_succ_exe + int(elem)
                if j == 9 and i == 1:
                    tasks_not_exe = tasks_not_exe + int(elem)
                if j == 10 and i == 1:
                    tasks_failed_delay = tasks_failed_delay + int(elem)
                if j == 12 and i == 1:
                    tasks_failed_mobility = tasks_failed_mobility + int(elem)
                if j == 15 and i == 1:
                    tasks_exec_cloud = tasks_exec_cloud + int(elem)
                if j == 17 and i == 1:
                    tasks_exec_edge = tasks_exec_edge + int(elem)

                #print('line[{}] = {}'.format(j, elem))
    if not (num == 0):
        print("Task tot " + str(generated_tasks / num))
        print("Task exec " + str(tasks_succ_exe / num))
        print("Task not exec " + str(tasks_not_exe / num))
        print("Task delay failed " + str(tasks_failed_delay / num))
        print("Task mobility failed " + str(tasks_failed_mobility / num))
        print("Task exec Cloud " + str(tasks_exec_cloud / num))
        print("Task exec edge " + str(tasks_exec_edge / num))

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



if __name__ == "__main__":
    main()
