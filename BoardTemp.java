import java.lang.Math;

public class Board{
	private static int WATER = 0;
	private static int FIRE = 1;
	
	private int boardSize = 8;  // Default to 8
	private Piece[][] pieces;

	private int currPlayer = Board.FIRE;
	private boolean pieceSelected = false;
	private boolean piecePlaced = false;
	
	private boolean sourcePosX, sourcePosY, destPosX, destPosY;
	
		
	public Board(boolean shouldBeEmpty){
		// Initialize pieces array to hold the pieces
		pieces = new Piece[boardSize][boardSize];
		
		if (shouldBeEmpty){
			// Make all pieces null so that the draw method does not draw the pieces.
		  	for(int i = 0; i < boardSize; ++i){
		  		for(int j = 0; j < boardSize; ++j){
		  			pieces[i][j] = null;
		  		}
		  	}
		} else {
			// The board is not empty.  So set the pieces at the right place
			setDefaultPieces();
		}
	}
//--------------------------------------------------------------------------------
	/**
	 * Sets the pieces at the initial position
	 * (boolean isFire, Board b, int x, int y, String type)
	 */
	private void setDefaultPieces(){
		// 0th row - Fire Pawn
		pieces[0][0] = new Piece(true, this, 0, 0, "pawn");
		pieces[2][0] = new Piece(true, this, 0, 2, "pawn");
		pieces[4][0] = new Piece(true, this, 0, 4, "pawn");
		pieces[6][0] = new Piece(true, this, 0, 6, "pawn");
		
		// 1st row - Fire Shield
		pieces[1][1] = new Piece(true, this, 1, 1, "shield");
		pieces[3][1] = new Piece(true, this, 1, 3, "shield");
		pieces[5][1] = new Piece(true, this, 1, 5, "shield");
		pieces[7][1] = new Piece(true, this, 1, 7, "shield");
		
		// 2nd row - Fire Bomb
		pieces[0][2] = new Piece(true, this, 2, 0, "bomb");
		pieces[2][2] = new Piece(true, this, 2, 2, "bomb");
		pieces[4][2] = new Piece(true, this, 2, 4, "bomb");
		pieces[6][2] = new Piece(true, this, 2, 6, "bomb");
		
		// 7th row - Water Pawn
		pieces[1][7] = new Piece(false, this, 7, 1, "pawn");
		pieces[3][7] = new Piece(false, this, 7, 3, "pawn");
		pieces[5][7] = new Piece(false, this, 7, 5, "pawn");
		pieces[7][7] = new Piece(false, this, 7, 7, "pawn");
		
		// 6th row - Water Shield
		pieces[0][6] = new Piece(false, this, 6, 0, "shield");
		pieces[2][6] = new Piece(false, this, 6, 2, "shield");
		pieces[4][6] = new Piece(false, this, 6, 4, "shield");
		pieces[6][6] = new Piece(false, this, 6, 6, "shield");
		
		// 5th row - Water Bomb
		pieces[1][5] = new Piece(false, this, 5, 1, "bomb");
		pieces[3][5] = new Piece(false, this, 5, 3, "bomb");
		pieces[5][5] = new Piece(false, this, 5, 5, "bomb");
		pieces[7][5] = new Piece(false, this, 5, 7, "bomb");
	}
//--------------------------------------------------------------------------------
	
	public Piece pieceAt(int x, int y){
		if ( (0 <= x) && (x <= this.boardSize) && (0 <= y) && (y <= this.boardSize) ){
			return pieces[x][y];
		}
		return null;
	}
	
	private String getImageFileForPiece(Piece p){
		String imageFile = "img/";
		if ( p.isBomb()){
			imageFile = imageFile + "bomb-";
		} else if ( p.isShield() ){
			imageFile = imageFile + "shield-";
		} else {
			imageFile = imageFile + "pawn-";
		}
			
		if ( p.isFire() ){
			imageFile = imageFile + "fire";
		} else {
			imageFile = imageFile + "water";
		}
		
		if ( p.isKing() ){
			imageFile = imageFile + "-crowned";
		}
		return imageFile + ".png";
	}
//----------------------------------------------------------------------------------------------------	
	/**
	 * Checks if the current player can select the cell (x,y)
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canSelect(int x, int y){
		// Look at the piece at x,y
		Piece pieceAtXY = pieceAt(x,y); 
		
		// If a piece was NOT selected already,
		if ( !this.pieceSelected ){  // Player just starts playing his turn
			//  and the user clicks on empty square, it is not selectable
			if ( pieceAtXY == null ) return false;

            // or if there is a piece at (x,y), can the current player select this piece?  Return true or false accordingly
			if ( pieceAtXY.isFire() && (this.currPlayer != Board.FIRE)) return false;
			if ( !pieceAtXY.isFire() && (this.currPlayer == Board.FIRE)) return false;
		} else{  // Current Player had already selected a piece.  Now he/she is moving the piece to a new square.
			// First of all the clicked position should be diagonally up or down with respect to the previously selected piece
			if((pieceAtXY != null) && (pieceAtXY.isFire() == Board.FIRE) && (!pieceSelected || !piecePlaced)){
				return true;
			} else if((pieceAtXY == null) && (pieceSelected) && (validMove(sourcePosX, sourcePosY, x, y)) && (!piecePlaced || this.hasCaptured())){
				return true;
			} else{
				return false;
			}
			
		}
		
		// If no piece is already selected, user clicks on a non-empty square:
		//     if the current player is fire and the piece in the clicked square is a "water" (or vice-versa), return false
		return true;
	}

	private boolean validMove(int initialX, int initialY, int finX, int finY){
		if(pieceAt(finX, finy) != null){
			return false;
		}

		if(pieceAt(intialX, initialY).isKing()){
			if((Math.abs(finX - initialX) == 1) && (Math.abs(finY - initialY) == 1)){
				return true;
			} 
			else if((Math.abs(finX - initialX)) == 2) && (Math.abs(finY - initialY) == 2){
				int midX = (finX + initialX)/2;
				int midY = (finY + initialY)/2;
				if((pieces[midX][midY] == null) || ((pieces[midX][midY].isFire()) == (pieceAt(initialX, initialY).isFire())){
					return false;
				} else{
					return true;
				}
			}
		}

		else if (!pieceAt(initialX, initialY).isKing()) {
			if (!pieceAt(initialX, initialY).isFire()) {
				if((Math.abs(finX - initialX) == 1) && (initialY - finY == 1){
					return true;
				} 
				else if((Math.abs(finX - initialX)) == 2) && (initialY - finY == 2)){
					int midX = (finX + initialX)/2;
					int midY = (finY + initialY)/2;
					if((pieces[midX][midY] == null) || ((pieces[midX][midY].isFire()) == (pieceAt(initialX, initialY).isFire())){
						return false;
					} 
					else {
						return true;
					}

				else{
					return false;
				}
			}
		}

			else{ 
				if((Math.abs(finX - initialX) == 1) && (finY - initalY == 1)){
					return true;
				} else if((Math.abs(finX - initialX)) == 2) && (finY - initialY == 2)){
					int midX = (finX + initialX)/2;
					int midY = (finY + initialY)/2;
					if((pieces[midX][midY] == null) || ((pieces[midX][midY].isFire()) == (pieceAt(initialX, initialY).isFire())){
						return false;
					} else{
						return true;
					}
				} else {
					return false;
				}
			}
		}
	}
//-----------------------------------------------------------------------------------------------------
	
	/**
	 * Draws the board according to the pieces setup by the Board settings.
	 */
    private void drawBoard() {
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                if ((i + j) % 2 == 0) StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
                else                  StdDrawPlus.setPenColor(StdDrawPlus.RED);
                StdDrawPlus.filledSquare(i + .5, j + .5, .5);
                StdDrawPlus.setPenColor(StdDrawPlus.WHITE);
 
                if (this.pieces[i][j] != null) {
                    StdDrawPlus.picture(i + .5, j + .5, this.getImageFileForPiece(pieces[i][j]), 1, 1);
                    
                }
            }
        }
    }	
    
    public void place(Piece p, int x, int y){
    	for (int i = 0; i < this.boardSize; i++) {
    		for (int j = 0; j < this.boardSize; j++) {
    			if (pieces[i][j] == p) {
    				pieces[i][j] = null;
    			}
    		}
    	}
    	pieces[x][y] = p;
    }

    public Piece remove(int x, int y){
    	if((x > 7) || (x < 0)){
    		System.out.println("Out of bounds");
    		return null;
    	}
    	else if((y > 7) || (y < 0)){
    		System.out.println("Out of bounds");
    		return null;
    	}
    	else{
    		Piece toRemove = this.pieces[x][y];
    		this.pieces[x][y] = null;
    		return toRemove;
    	}
    }

    public boolean canEndTurn(){
    	return pieceSelected && piecePlaced;
    }
    
    public void endTurn(){
		// Change the player turn
		if (currPlayer == Board.FIRE) {
			currPlayer = Board.WATER;
		}else{
			currPlayer = Board.FIRE;
		}
		pieceSelected = false;
		piecePlaced = false;
    }

    public String winner(){
    	int numFire = 0;
    	int numWater = 0;
    	for (int i = 0; i < this.boardSize; i++) {
    		for (int j = 0; j < this.boardSize; j++) {
    			if(pieces[i][j] != null){
    				if(pieces[i][j].isFire()){
    					numFire++;
    				}
    				else{
    					numWater++;
    				}
    			}
    		}
    	}
    	if((numFire == 0) && (numWater != 0)){
    		return "Water wins!";
    	}
    	else if((numFire != 0 ) && (numWater == 0)){
    		return "Fire wins!";
    	}
    	else if((numFire == 0) && (numWater == 0)){
    		return "It's a tie!";
    	}
    	else{
    		return null;
    	}
    }
//--------------------------------------------------------------------------------
	public static void main(String args[]){

        /** Monitors for mouse presses. Wherever the mouse is pressed,
            a new piece appears. */
        Board b = new Board(false);
        StdDrawPlus.setXscale(0, b.boardSize);
        StdDrawPlus.setYscale(0, b.boardSize);
        
        while(true) {
            b.drawBoard();
            
            if (StdDrawPlus.mousePressed()) {
                double x = StdDrawPlus.mouseX();
                double y = StdDrawPlus.mouseY();
                System.out.print("X = " + (int) x + " Y = " + (int) y + "   ");
            }       
            
            if( StdDrawPlus.isSpacePressed() ){
            	if ( b.canEndTurn() ){
            		b.endTurn();
            	}
            }
            
            StdDrawPlus.show(100);
        }
    }

}