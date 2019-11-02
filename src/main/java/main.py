import numpy as np
import random
import plotly.express as px
import pandas as pd


def show(xs, ys, title, x_label, y_label):
    df = pd.DataFrame(dict(
        x=xs,
        y=ys))
    fig = px.scatter(df, x="x", y="y",
                     title=title,
                     labels={x_label: y_label})
    fig.show()


def merge(g, x, y):
    g_copy = np.array(g, copy=True)

    for i in range(len(g)):
        g_copy[i][x] = g_copy[i][x] + g_copy[i][y]
    g_copy = np.delete(g_copy, y, 1)

    g_copy[x] = np.add(g_copy[x], g_copy[y])
    g_copy = np.delete(g_copy, y, 0)

    for i in range(len(g_copy)):
        g_copy[i][i] = 0

    return g_copy


def dist_function(g, chosen, next_vertices):
    path_sum = 0
    for i in range(len(chosen)):
        path_sum += g[next_vertices][chosen[i]]
    return path_sum


def possible(g, chosen):
    possible_vertices = []
    for i in range(len(g)):
        if i not in chosen:
            possible_vertices.append(i)
    return possible_vertices


def mao(g):
    chosen = [0]
    while len(chosen) != len(g):
        possible_vertices = possible(g, chosen)
        next_vertex = possible_vertices[0]
        vertex_to_chosen = dist_function(g, chosen, next_vertex)
        for i in range(len(possible_vertices)):
            possible_vertex = possible_vertices[i]
            val = dist_function(g, chosen, possible_vertex)
            if val > vertex_to_chosen:
                vertex_to_chosen = val
                next_vertex = possible_vertex
        chosen.append(next_vertex)
    return chosen


def degree(g, vertex):
    count = 0
    for j in range(len(g)):
        count += g[vertex][j]
    return count


def lamb(g):
    if len(g) == 2:
        return g[0][1]
    else:
        max_adj = mao(g)
        x = max_adj[-2]
        y = max_adj[-1]
        a = degree(g, y)
        g_xy = merge(g, x, y)
        b = lamb(g_xy)
        return min(a, b)


def generate_graph(m):
    g = [0] * 20
    for i in range(0, 20):
        g[i] = [0] * 20
    for n in range(0, m):
        i = random.randint(0, 19)
        j = random.randint(0, 19)
        if i == j or g[i][j] == 1:
            n -= 1
            continue
        else:
            g[i][j] = 1
    return g


def helper(arr_avg):
    sort_avg = sorted(arr_avg)
    spread_lamda = []
    for i in range(len(sort_avg)):
        vertex = sort_avg[i]
        smallest = smallfind(sort_avg, vertex)
        largest = largefind(sort_avg, vertex)
        sub = largest - smallest
        spread_lamda.append(sub)
    return spread_lamda


def smallfind(sort_avg, vertex):
    for i in range(len(sort_avg)):
        if sort_avg[i] == vertex:
            a = (i * 3) + 19
            return a


def largefind(sort_avg, vertex):
    i = len(sort_avg) - 1
    while i != 0:
        if sort_avg[i] == vertex:
            b = (i * 3) + 19
            return b
        i = i - 1


def main():
    arr_avg = []
    for m in range(19, 190, 3):
        edge_connectivity_five = []
        for i in range(0, 5):
            edge_connectivity_five.append(lamb(generate_graph(m)))
        arr_avg.append(sum(edge_connectivity_five) / len(edge_connectivity_five))
    show(range(19, 190, 3), arr_avg, "λ(G) as a function of m", "m", "lamda(G)")

    something = helper(arr_avg)
    show(arr_avg, something, "s(λ) as a function of λ,", "m", "lamda(G)")


main()
