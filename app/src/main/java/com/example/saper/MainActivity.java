package com.example.saper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import Message.Message;
import Victory.Victory;

import static java.lang.Integer.parseInt;


public class MainActivity extends Activity implements OnClickListener, OnLongClickListener {
    private int WIDTH = 10; // ширина поля
    private int HEIGHT = 10; // высота поля
    int numMines = 10; // количество мин

    private Button[][] cells; // массив клеток
    private boolean[][] flags = new boolean[WIDTH][HEIGHT];
    boolean[][] revealed = new boolean[WIDTH][HEIGHT]; // массив открытых клеток
    int[][] mines = new int[WIDTH][HEIGHT]; // массив мин

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cells);

        for (int j = 0; j < HEIGHT; j++)
            for (int i = 0; i < WIDTH; i++) {
                mines[i][j]=0;
                revealed[i][j]=false;
                flags[i][j]=false;
            }

        makeCells();
        generate();
        if(Arrays.deepEquals(flags, mines)){
            Victory.showMessage(this, "Все разминировано!");
        }
    }




//создаем клетки
    void makeCells() {
        cells = new Button[WIDTH][HEIGHT];
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(WIDTH);
        for (int j = 0; j < HEIGHT; j++)
            for (int i = 0; i < WIDTH; i++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                cells[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                cells[i][j].setOnClickListener(this);
                cells[i][j].setOnLongClickListener(this);
                cells[i][j].setTag(j + "," + i);
                cellsLayout.addView(cells[i][j]);
            }
    }


//рандомно генерируем поле с минами
    void generate() {
        int c=0;
        while(c<numMines){//заполняем поле минами до определенного количесвта
            int x=(int)(Math.random() * (WIDTH));
            int y=(int)(Math.random() * (HEIGHT));
            if(mines[x][y] != 1) {
                mines[x][y] = 1;
                cells[x][y].setText("M");
                c++;
            }
            //cells[x][y].setTextColor(0);
        }
    }

// считывание координат клетки
    int getX(View v) { return Integer.parseInt(((String) v.getTag()).split(",")[1]); }

    int getY(View v) { return Integer.parseInt(((String) v.getTag()).split(",")[0]); }

//реакция на клик мыши

boolean firstClick=true; // первый клик никогда не мина
    @Override
    public void onClick(View v) {
        Button tappedCell = (Button) v;
        int tappedX = getX(tappedCell); // считываем координаты клетки
        int tappedY = getY(tappedCell);

        tappedCell.setEnabled(false); //выключаем нажатую клетку
        tappedCell.setBackgroundColor(Color.WHITE); // окрашиваем клетку в белый цвет
        //Message.showMessage(this, Integer.toString(tappedX) + " " + Integer.toString(tappedY));
        if (firstClick) { // избегаем первого клика на мину
            firstClick=false;
           do {
                clearMines();
                generate();
            } while (minesNear(tappedX,tappedY)!=0); // находим ситуацию когда мин вокруг нет
        }
        if (mines[tappedX][tappedY] == 1) { //если нажали на мину
            Message.showMessage(this, "MINA");// ПРОИГРЫШ
            clearMines();  // все заново
            firstClick=true;
            makeCells();
            generate();
        } else
         {//Не проиграли? продолжаем игру

            reveal(tappedX, tappedY);
        }
    }






    @Override
    public boolean onLongClick(View v) {
        Button tappedCell = (Button) v;
        int tappedX = getX(tappedCell);
        int tappedY = getY(tappedCell);
        if (flags[tappedX][tappedY]){
            cells[tappedX][tappedY].setBackgroundResource(R.color.grey);
            flags[tappedX][tappedY]=false;
        }else {
            cells[tappedX][tappedY].setBackgroundColor(Color.RED);
            flags[tappedX][tappedY]=true;
        }

        return false;
    }





    //Очищаем поле от мин
    void clearMines() {
        for (int y=0; y<HEIGHT; y++) {
            for (int x=0; x<WIDTH; x++) {
                cells[x][y].setText("");
                mines[x][y] = 0;
                revealed[x][y] = false;
                flags[x][y]=false;
            }
        }
    }


    //Количество мин рядом
    boolean outBorder(int x,int y){
        return x<0||y<0||x>=WIDTH||y>=HEIGHT; // если уйдет за края то false/0
    }

    int minesNear(int x, int y) {

        if(outBorder(x,y))return 0;

        int i=0;//количество мин
        for (int nearX=-1; nearX<=1; nearX++) { //проверяем количество мин вокруг клетки
            for (int nearY=-1; nearY<=1; nearY++) {
                if (outBorder(nearX+x, nearY+y))continue;
                i+=mines[nearX+x][nearY+y]; // если мина то +1(т.к. мина =1, то можно +mines)
            }
        }
        return i;
    }

    // раскрываем квадраты
    void reveal(int x, int y){
        if(outBorder(x,y))return; // end если за границей
        if(revealed[x][y])return; // end если квадрат уже раскрыт
        revealed[x][y]=true; // раскрываем квадрат
        cells[x][y].setBackgroundColor(Color.WHITE);
        cells[x][y].setEnabled(false);
        if(minesNear(x,y)!=0){
            cells[x][y].setText(Integer.toString(minesNear(x,y)));
            return; // если рядом мина то end
        }
        reveal(x-1,y-1); //если рядом нет мин то смотрим соседние клетки
        reveal(x-1,y+1);
        reveal(x+1,y-1);
        reveal(x+1,y+1);
        reveal(x-1,y);
        reveal(x+1,y);
        reveal(x,y-1);
        reveal(x,y+1);
    }


}