package com.company;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/*
Leet Code question The Maze III
Link: https://www.lintcode.com/problem/the-maze-iii/description
 */

public class TheMaze {
    public static void main(String[] args) {
        int[][] maze = new int[][]{
                new int[]{0,0,0},
                new int[]{0,0,0},
                new int[]{0,0,0},
        };
        int[] ball = new int[]{0,0};
        int[] hole = new int[]{1,2};

        boolean[][] visited = new boolean[maze.length][maze[0].length];
        Solution solver = new Solution();
        String result = solver.findSolution(maze,ball,hole,visited);

        System.out.println(result);
    }
}


class Solution{

    // use uniform cost search to find lowest-cost solution
    // use custom comparator SortStatePair to make sure that for same-cost solution, the lexicographically smaller one is checked first
    public String findSolution(int[][] maze, int[] state, int[] hole, boolean[][] visited){
        PriorityQueue<MyPair> q = new PriorityQueue<MyPair>(10,new SortStatePair());
        q.add(new MyPair(state,"",0));

        while (q.size()>0){
            MyPair node = q.poll();
            visited[node.state[0]][node.state[1]] = true;

            if (Arrays.equals(node.state,hole)){
                return node.actions;
            }

            Move[] nextMoves = next(node.state,maze,hole);
            for (int i=0;i<nextMoves.length;i++){
                Move nextMove = nextMoves[i];
                if (Arrays.equals(nextMove.state,node.state) || visited[nextMove.state[0]][nextMove.state[1]]){
                    continue;
                }
                q.add(new MyPair(
                        nextMove.state,
                        node.actions + Character.toString(nextMove.action),
                        node.cost+nextMove.cost
                ));
            }
        }

        return "impossible";
    }

    public Move[] next(int[] state, int[][] maze, int[]hole){
        // lexicographical order of action: d, l, r, u
        return new Move[]{
                moveDown(state,maze,hole),
                moveLeft(state,maze,hole),
                moveRight(state,maze,hole),
                moveUp(state,maze,hole),
        };
    }

    public Move moveDown(int[] state, int[][] maze, int[]hole){
        int x  = state[0];
        int y  = state[1];
        int cost = 0;
        while (x<maze.length-1 && maze[x+1][y] == 0){
            x++;
            cost++;
            if (x == hole[0] && y == hole[1]){
                break;
            }
        }
        return new Move(new int[]{x,y},'d',cost);
    }

    public Move moveUp(int[] state, int[][] maze, int[]hole){
        int x  = state[0];
        int y  = state[1];
        int cost = 0;
        while (x>0 && maze[x-1][y] == 0){
            x--;
            cost++;
            if (x == hole[0] && y == hole[1]){
                break;
            }
        }
        return new Move(new int[]{x,y},'u',cost);
    }

    public Move moveLeft(int[] state, int[][] maze, int[]hole){
        int x  = state[0];
        int y  = state[1];
        int cost = 0;
        while (y>0 && maze[x][y-1] == 0){
            y--;
            cost ++;
            if (x == hole[0] && y == hole[1]){
                break;
            }
        }
        return new Move(new int[]{x,y},'l',cost);
    }

    public Move moveRight(int[] state, int[][] maze, int[]hole){
        int x  = state[0];
        int y  = state[1];
        int cost = 0;
        while (y<maze[0].length-1 && maze[x][y+1] == 0){
            y++;
            cost++;
            if (x == hole[0] && y == hole[1]){
                break;
            }
        }
        return new Move(new int[]{x,y},'r',cost);
    }
}

class MyPair{
    public int[] state;
    public String actions;
    public int cost;

    public MyPair(int[] s, String a, int cost){
        this.state = s;
        this.actions = a;
        this.cost = cost;
    }
}

class Move{
    public int[] state;
    public char action;
    public int cost;

    public Move(int[] state, char action, int cost){
        this.state = state;
        this.action = action;
        this.cost = cost;
    }
}

class SortStatePair implements Comparator<MyPair> {
    public int compare(MyPair a, MyPair b){
        if (a.cost != b.cost){
            return a.cost - b.cost;
        }
        return a.actions.compareTo(b.actions);
    }
}