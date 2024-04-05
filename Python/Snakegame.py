import tkinter
import random

rows= 25
cols =25
Tile_Size = 25

Window_Width= Tile_Size * cols
Window_Height= Tile_Size * rows

class Tile:
    def __init__(self,x,y):
        self.x = x
        self.y = y



#game window 
window = tkinter.Tk()
window.title("Snake Game")

# Won't allow window to be resized
window.resizable(False, False)

canvas = tkinter.Canvas(window,bg ="black", width = Window_Width, height = Window_Height, borderwidth= 0, highlightthickness=0) 
canvas.pack()
window.update()

# Center the window
window_width = window.winfo_width()
window_height = window.winfo_height()
screen_width = window.winfo_screenwidth()
screen_height = window.winfo_screenheight()

window_x = int((screen_width/2) -(window_width/2))
window_y = int((screen_height/2) -(window_height/2))
window.geometry(f"{window_width}x{window_height}+{window_x}+{window_y}")



#initialize game
snake =Tile(5*Tile_Size,5*Tile_Size)#single tile, head of snake
food = Tile(10*Tile_Size,10*Tile_Size)#single tile, food for snake
snake_body = [] 
velocityX = 0
velocityY = 0
game_over = False
score = 0

def change_direction(e): #e =event  
    #print(e)
   # print(e.keysym)
    global velocityX,velocityY, game_over
    if(game_over):
        return
    if(e.keysym == "Up" and velocityY != 1):
        velocityX = 0
        velocityY = -1
    elif(e.keysym == "Down" and velocityY != -1):
        velocityX = 0
        velocityY = 1
    elif(e.keysym == "Left" and velocityX != 1):
        velocityX = -1
        velocityY = 0
    elif(e.keysym == "Right" and velocityX != -1):
        velocityX = 1
        velocityY = 0

def move():
    global snake,food , snake_body,game_over , score
    if(game_over):
        return
    if(snake.x < 0 or snake.x >= Window_Width or snake.y < 0 or snake.y >= Window_Height):
        game_over = True
        return
    for tile in snake_body:
        if(snake.x == tile.x and snake.y == tile.y):
            game_over = True
            return

    #collision
    if (snake.x == food.x and snake.y == food.y):
        snake_body.append(Tile(food.x,food.y))
        food.x = Tile_Size * random.randint(0,cols-1) 
        food.y = Tile_Size * random.randint(0,rows-1) 
        score += 1

    # update snake body
    for i in range(len(snake_body)-1,-1, -1):
        tile = snake_body[i]
        if(i==0):
            tile.x = snake.x
            tile.y = snake.y
        else:
            prev_tile = snake_body[i-1]
            tile.x = prev_tile.x
            tile.y = prev_tile.y
    snake.x += velocityX * Tile_Size
    snake.y += velocityY * Tile_Size

def draw():
    global snake , food , snake_body, game_over, score
    move()

    canvas.delete("all")
    #draw snake
    canvas.create_rectangle(snake.x,snake.y,snake.x+Tile_Size,snake.y+Tile_Size,fill="lime green")
    
    for tile in snake_body:
        canvas.create_rectangle(tile.x,tile.y,tile.x+Tile_Size,tile.y+Tile_Size,fill="lime green")

    #draw food
    canvas.create_rectangle(food.x,food.y,food.x+Tile_Size,food.y+Tile_Size,fill="red")

    if(game_over):
        canvas.create_text(Window_Width/2,Window_Height/2,text= f"Game Over: {score}", fill="white", font="Arial 24")
    else:
        canvas.create_text(30,20,text=f"Score: {score}", fill="white", font="Arial 12")
    window.after(100,draw)

draw()
window.bind("<KeyRelease>", change_direction)
window.mainloop()