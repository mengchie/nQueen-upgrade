import java.io.*;
import java.util.*;

public class homework {
	private static long startTime = System.nanoTime();

	public static void main(String[] args) throws IOException {
		
		FileReader in = null;
		String algorithm = "";
		List<String> tree = new ArrayList<>();
		int[][] positions;
        List<String> sol = new ArrayList<>();
        // String[] solu;
        List<String> originalSol = new ArrayList<>();
        List<String> passedP = new ArrayList<>();
		int n = 0;
		int lizardNum = 0;
		int lizardTotal = 0;
		try {
			in = new FileReader("input.txt");
			BufferedReader br = new BufferedReader(in); 
			String s;
			int line = 0;
	 		while ((s = br.readLine()) != null) {
	 			if(line == 0) {
	 				algorithm = s;
	 			}
	 			if(line == 1) {
	 				n = Integer.parseInt(s);
	 			}
	 			if(line == 2) {
	 				lizardNum = Integer.parseInt(s);
	 				lizardTotal = Integer.parseInt(s);
	 			}
	 			if(line > 2) {
	 				tree.add(s);
	 				sol.add(s);
	 				originalSol.add(s);
	 				passedP.add(s);
	 			}
	        	line++;
	        }
	        if(algorithm.equals("DFS")) {
	        	positions = new int[n][n];
	        	System.out.println("in dfs");
	        	FileWriter f1 = new FileWriter("output.txt");
	        	if(dfs(positions, tree, sol, n, 0, 0, lizardNum)) {
	        		f1.write("OK\n"); 
					for(int i = 0; i < sol.size(); i++) {
						if(i == sol.size()-1) {
							f1.write(sol.get(i));
						} else {
							f1.write(sol.get(i) + "\n"); 
						}	
					}
					
					f1.close();
	        	} else {
	        		f1.write("FAIL");
	        	}
	        	System.out.println("finish dfs");
	        }

	        if(algorithm.equals("BFS")) {
	        	
	        	Queue<int[]> targetPs = new ArrayDeque<>();
	        	Queue<int[][]> positionsQ = new ArrayDeque<>();
	        	// Queue<String[]> solQ= new ArrayDeque<>();
	        	Queue<Integer> lizardQ = new ArrayDeque<>();
	        	System.out.println("in bfs");
	        	for(int i = 0; i < n; i++) {
	        		for(int j = 0; j < n; j++) {
	        			int[][] inPositionsQ = new int[n][n];
	        			for(int r = 0; r < n; r++){
	        				for(int c = 0; c < n; c++) {
	        					if(tree.get(r).charAt(c) == '2') {
	        						inPositionsQ[r][c] = 2;
	        					}
	        				}
	        			}
	        			positionsQ.add(inPositionsQ);
	        			int[] targetP = new int[]{i,j};
	        			// i = row, j = col
	        			targetPs.add(targetP);
	        			lizardQ.add(lizardNum);
	        			
	        		}
	        	}

	        	FileWriter f1 = new FileWriter("output.txt");
	        	if(bfs(lizardQ, targetPs, positionsQ, tree, n, sol)) {
	        		f1.write("OK\n");
	        		for(int i = 0; i < sol.size(); i++) {
						if(i == sol.size()-1) {
							f1.write(sol.get(i));
						} else {
							f1.write(sol.get(i) + "\n"); 
						}	
					}
	        	} else {
	        		f1.write("FAIL");
	        	}
				f1.close();
	        	System.out.println("finish bfs");

	        }

	        if(algorithm.equals("SA")) {
	        	System.out.println("in SA");
	        	boolean hasSol = false;
	        	// System.out.println("startTime: " + startTime/1000000000 + " System.nanoTime()" + System.nanoTime()/1000000000);
	        	while((System.nanoTime()/1000000000 - startTime/1000000000) < 180) {
	        		System.out.println("in while loop SA");
	        		positions = new int[n][n];
					int[] rows = new Random().ints(0,n).limit(lizardNum).toArray();
					int[] cols = new Random().ints(0,n).limit(lizardNum).toArray();
					int conflictNum = 0;
					float temperature = 35.0F;

					for(int i = 0; i < tree.size(); i++) {
						for(int j = 0; j < tree.get(i).length(); j++) {
							if(tree.get(i).charAt(j) == '2') {
								positions[i][j] = 2;
							}	
						}
					}

					for(int i = 0; i < rows.length; i++) {
						if(positions[rows[i]][cols[i]] != 1 && tree.get(rows[i]).charAt(cols[i]) != '2') {
							positions[rows[i]][cols[i]] = 1;
						} else {
							int newRow = new Random().nextInt(n);
							int newCol = new Random().nextInt(n);
							while(positions[newRow][newCol] == 1 || tree.get(newRow).charAt(newCol) == '2') {
								// positions[newRow][newCol] = 1;
								newRow = new Random().nextInt(n);
								newCol = new Random().nextInt(n);
							}
							positions[newRow][newCol] = 1;
							rows[i] = newRow;
							cols[i] = newCol;
						}
					}
					for(int i = 0; i < rows.length; i++) {
						System.out.println("row: " + rows[i] + " col: " + cols[i]);
					}
					
					
					for(int i = 0; i < positions.length; i++) {
						for(int j = 0; j < positions[0].length; j++) {
							if(positions[i][j] == 1) {
								conflictNum += isConflict(i, j, tree, positions);
							}
						}
					}
					
					if(sa(temperature, positions, rows, cols, conflictNum, tree, sol) == true) { 
						hasSol = true;
						FileWriter f1 = new FileWriter("output.txt");
						f1.write("OK\n");
						for(int i = 0; i < sol.size(); i++) {
							if(i == sol.size()-1) {
								f1.write(sol.get(i));
							} else {
								f1.write(sol.get(i) + "\n"); 
							}
						}
						f1.close();
						break;
					} 
	        	}
	        	if(hasSol == false) {
	        		FileWriter f1 = new FileWriter("output.txt");
					f1.write("FAIL");
					f1.close();
	        	}
	        	
	        }


	        
	        for(int i = 0; i < sol.size(); i++) {
	        	System.out.println(sol.get(i));
	        }

			long endTime = System.nanoTime();

			long duration = (endTime - startTime);
			long remain = duration%1000000000;
			System.out.println("duration: " + duration/1000000000 + "s " + remain*1000 + "ms");

		} finally {
	    	if (in != null) {
	    		in.close();
	        }
	    }
	}

	public static boolean sa(float temperature, int[][] positions, int[] rows, int[] cols, int conflictNum, List<String> tree, List<String> sol) {
		
		int preEnergy = conflictNum;
		
		while(temperature > 0) {
			int newEnergy = 0;
			List<Integer> rowList = new ArrayList<>();
			List<Integer> colList = new ArrayList<>();
			for(int i = 0; i < rows.length; i++) {
				// System.out.println("in checkConfNum");
				int checkConfNum = isConflict(rows[i], cols[i], tree, positions);
				if(checkConfNum != 0) {
					// newEnergy += checkConfNum;
					while(checkConfNum > 0) {
						rowList.add(rows[i]);
						colList.add(cols[i]);
						checkConfNum--;
					}
				} else {
					rowList.add(rows[i]);
					colList.add(cols[i]);
				}
			}
			int randomIndex = new Random().nextInt(rowList.size());
			int[] nRow = rows.clone();
			int[] nCol = cols.clone();
			int rowIndex = rowList.get(randomIndex);
			int colIndex = colList.get(randomIndex);
			int tempR = 0;
			int tempC = 0;
			boolean isDup = true;
			while(isDup) {
				tempR = new Random().nextInt(positions.length);
				tempC = new Random().nextInt(positions[0].length);
				if(positions[tempR][tempC] != 1 && positions[tempR][tempC] != 2) {
					isDup = false;
					int trueIndex = 0;
					for(int i = 0; i < nRow.length; i++) {
						if(nRow[i] == rowIndex && nCol[i] == colIndex) {
							nRow[i] = tempR;
							nCol[i] = tempC;
						}	
					}
				}
			}

			int[][] nPositions = new int[positions.length][positions[0].length];
			for(int i = 0; i < positions.length; i++) {
				for(int j = 0; j < positions[0].length; j++) {
					nPositions[i][j] = positions[i][j];
				}
			}
			nPositions[rowIndex][colIndex] = 0;
			nPositions[tempR][tempC] = 1;

			for(int i = 0; i < nRow.length; i++) {
				newEnergy += isConflict(nRow[i], nCol[i], tree, nPositions);
			}
			if(newEnergy != 0) {
				if(preEnergy - newEnergy >= 0) {
					temperature = temperature - 0.00001F;
					positions = nPositions;
					rows = nRow;
					cols = nCol;
					preEnergy = newEnergy;
				} else {
					if(Math.pow((double)Math.exp(1.0), (double)(((double)preEnergy - (double)newEnergy)/(double)temperature)) < 0.5) {
						temperature = temperature - 0.00001F;
						positions = nPositions;
						rows = nRow;
						cols = nCol;
						preEnergy = newEnergy;
					} else {
						temperature = temperature - 0.00001F;
					}
				}
				
			} else {
				rows = nRow;
				cols = nCol;
				positions = nPositions;
				for(int i = 0; i < positions.length; i++) {
					String ans = "";
					for(int j = 0; j < positions[0].length; j++) {
						if(positions[i][j] == 0) {
							ans += "0";
						} else if(positions[i][j] == 1) {
							ans += "1";
						} else {
							ans += "2";
						}
					}
					sol.set(i, ans);
				}
				System.out.println("SA got an answer!!!");
				return true;
			}

		}
		
		System.out.println("Fail to find an answer in SA!!!");
		return false;

	}

	public static boolean bfs(Queue<Integer>lizardQ, Queue<int[]> targetPs, Queue<int[][]> positionsQ, List<String> tree, int n, List<String> sol) {
		
		
		while(targetPs.peek() != null) {
			int[] targetP = targetPs.poll();

			int lizardNum = lizardQ.poll();

			int[][] positions = positionsQ.poll();
			int[][] position = new int[n][n];
			for(int i = 0; i < position.length; i++) {
				for(int j = 0; j < position[i].length; j++) {
					position[i][j] = positions[i][j];
				}
			}
			int row = targetP[0];
			int col = targetP[1];
			int numLizard = lizardNum;

			if(validPosition(row, col, tree, position)) {

				numLizard--;
				position[row][col] = 1;

		        if(numLizard == 0) {
		        	for(int r = 0; r < n; r++) {
		        		String inSol = "";
		        		for(int c = 0; c < n; c++) {
		        			inSol += position[r][c];
		        		}
		        		sol.set(r, inSol);
		        	}
		        	
		        	System.out.println("bfs successful...");
					return true;
		        }
		        int[][] posForchange = new int[n][n];
		        for(int r = 0; r < n; r++) {
	        		for(int c = 0; c < n; c++) {
	        			posForchange[r][c] = position[r][c];
	        		}
	        	}
		        changeBfsSol(position, posForchange, n);
				

		        if(row < n-1) {
		        	for(int i = row; i < n; i++) {
		        		if(i == row && col < n-1) {
		        			for(int j = col+1; j < n; j++) {
		        				if(posForchange[i][j] == 0) {
			        				lizardQ.add(numLizard);
			        				targetP = new int[]{i,j};
				        			
				        			targetPs.add(targetP);
				        			positionsQ.add(position);
			        			}
		        			}
		        		} else {
		        			for(int j = 0; j < n; j++) {
			        			if(posForchange[i][j] == 0) {
			        				lizardQ.add(numLizard);
			        				targetP = new int[]{i,j};
				        			
				        			targetPs.add(targetP);
				        			positionsQ.add(position);
			        			}
			        		}
		        		}
		        		
		        	}
		        } else {
		        	if(col < n-1) {
		        		for(int j = col+1; j < n; j++) {
		        			if(posForchange[n-1][j] == 0) {
		        				lizardQ.add(numLizard);
		        				targetP = new int[]{n-1,j};
			        			
			        			targetPs.add(targetP);
			        			positionsQ.add(position);
		        			}
		        		}
		        	}
		        	
		        }
				
			} 

		}

		System.out.println("Queue is empty");
		System.out.println("Fail to find answer");
		return false;
	}

	public static boolean dfs(int[][] positions, List<String> tree, List<String> sol, int n, int row, int col, int lizardNum) {
        if(lizardNum == 0) {
        	System.out.println("RETURN TRUE!! LIZARDNUM == 0");
            return true;
        }
        if(row == n) return false;

    	if(validPosition(row, col, tree, positions)) {
    		lizardNum--;
    		StringBuilder treeLine = new StringBuilder(sol.get(row));
    		StringBuilder backTrackLine = new StringBuilder(treeLine);
            treeLine.setCharAt(col,'1');
            sol.set(row, treeLine.toString());

    		
            positions[row][col] = 1;

            if(col == n-1) {
            	if(dfs(positions, tree, sol, n, row+1, 0, lizardNum)) {
		        	return true;
		        } else {
		        	sol.set(row, backTrackLine.toString());
					positions[row][col] = 0;
					lizardNum++;
		        }
            } else {
            	if(dfs(positions, tree, sol, n, row, col+1, lizardNum)) {
		        	return true;
		        } else {
		        	sol.set(row, backTrackLine.toString());
					positions[row][col] = 0;
					lizardNum++;
		        }
            }

    	}
    	if(col == n-1) {
    		if(dfs(positions, tree, sol, n, row+1, 0, lizardNum)) {
	        	return true;
	        }
	        return false;
    	} else {
    		if(dfs(positions, tree, sol, n, row, col+1, lizardNum)) {
	        	return true;
	        }
	        return false;
    	}
    	
    }
    public static int isConflict(int row, int col, List<String> tree, int[][] positions) {
    	int conflictNum = 0;
    	if(tree.get(row).charAt(col) == '2') {
    		System.out.println("Tree conflict!! Can't happen!! GO CHECK YOUR BUGS!!!");	
    	} 
    	
    	
        for(int pRow = positions.length-1; pRow >= 0; pRow--) {
        	for(int pCol = positions[pRow].length-1; pCol >= 0; pCol--) {
        		
        		
        		if(positions[pRow][pCol] == 1) {
        			if(pRow == row && pCol == col) {
	        			continue;
	        		}
	        		
	        		if(pRow == row) {
		        		boolean isTree = false;
		        		int tCol = pCol;
		        		int inCol = col;
		        		if(inCol < tCol){
		        			int temp = inCol;
		        			inCol = tCol;
		        			tCol = temp;
		        		}
		        		for(int checkCol = inCol-1; checkCol > tCol; checkCol--) {

		        			if(tree.get(row).charAt(checkCol) == '2') {
		        				isTree = true;
		        			}
		        		}
		        		if(isTree == false) {
		        			conflictNum++;
		        		}
		        	}


	        		if(pCol == col) {
		        		boolean isTree = false;
		        		int inRow = row;
		        		int tRow = pRow;
		        		if(row < tRow) {
		        			int temp = inRow;
		        			inRow = tRow;
		        			tRow = temp;
		        		}
		        		for(int checkRow = inRow-1; checkRow > tRow; checkRow--) {
		        			if(tree.get(checkRow).charAt(col) == '2') {
		        				isTree = true;
		        			}
		        		}
		        		if(isTree == false) {
		        			conflictNum++;
		        		}
		        	}
		            if(pCol - col == (pRow - row) * -1) {
		            	int checkRow;
		            	int checkCol;
		            	int times;
		            	if(row > pRow) {
							checkRow = row - 1;
							checkCol = col + 1;
							times = row - pRow - 1;
		            	} else {
		            		checkRow = pRow - 1;
		            		checkCol = pCol + 1;
		            		times = pRow - row - 1;
		            	}

		                boolean isTree = false;
		                while(times > 0) {
		                	if(tree.get(checkRow).charAt(checkCol) == '2') {
		        				isTree = true;
		        			}
		                	checkCol++;
		                	checkRow--;
		                	times--;
		                }
		        		if(isTree == false) {
		        			conflictNum++;
		        		}
		            }
		            if(pCol - col == pRow - row) {
		            	int checkRow;
		            	int checkCol;
		            	int times;
		            	if(row > pRow) {
							checkRow = row - 1;
							checkCol = col - 1;
							times = row - pRow - 1;
		            	} else {
		            		checkRow = pRow - 1;
		            		checkCol = pCol - 1;
		            		times = pRow - row - 1;
		            	}

		                boolean isTree = false;
		                while(times > 0) {
		                	if(tree.get(checkRow).charAt(checkCol) == '2') {
		        				isTree = true;
		        			}
		                	checkCol--;
		                	checkRow--;
		                	times--;
		                }
		        		if(isTree == false) {
		        			conflictNum++;
		        		}
		            }

        		}
        	}
        	
        }
        return conflictNum;
    }

    public static void changeBfsSol(int[][] positions, int[][] sol, int n) {
    	
        for(int pRow = positions.length-1; pRow >= 0; pRow--) {
        	for(int pCol = positions[pRow].length-1; pCol >= 0; pCol--) {

	        	if(positions[pRow][pCol] == 1) {
		            int row = pRow;
		            int col = pCol + 1;
	        		//go right
		            while(col < n) {
		            	if(sol[row][col] == 2) break;
		            	if(sol[row][col] == 0) {
		            		sol[row][col] = 3;
		            	}
		            	col++;
		            }

		            row = pRow;
		            col = pCol - 1;

	        		//go left
		            while(col >= 0) {
		            	if(sol[row][col] == 2) break;
		            	if(sol[row][col] == 0) {
		            		sol[row][col] = 3;
		            	}
		            	col--;
		            }

		            row = pRow - 1;
		            col = pCol;

	        		//go up
		            while(row >= 0) {
		            	if(sol[row][col] == 2) break;
		            	if(sol[row][col] == 0) {
		            		sol[row][col] = 3;
		            	}
		            	row--;
		            }
	        		
	        		row = pRow + 1;
		            col = pCol;

	        		//go down
		            while(row < n) {
		            	if(sol[row][col] == 2) break;
		            	if(sol[row][col] == 0) {
		            		sol[row][col] = 3;
		            	}
		            	row++;
		            }

		            row = pRow - 1;
		            col = pCol + 1;

	        		//go right up
		            while(col < n && row >= 0) {
		            	if(sol[row][col] == 2) break;
		            	if(sol[row][col] == 0) {
		            		sol[row][col] = 3;
		            	}
		            	col++;
		            	row--;
		            }

		            row = pRow + 1;
		            col = pCol + 1;

	        		//go right down
		            while(col < n && row < n) {
		            	if(sol[row][col] == 2) break;
		            	if(sol[row][col] == 0) {
		            		sol[row][col] = 3;
		            	}
		            	col++;
		            	row++;
		            }

		            row = pRow - 1;
		            col = pCol - 1;

	        		//go left up
		            while(col >= 0 && row >= 0) {
		            	if(sol[row][col] == 2) break;
		            	if(sol[row][col] == 0) {
		            		sol[row][col] = 3;
		            	}
		            	col--;
		            	row--;
		            }

		            row = pRow + 1;
		            col = pCol - 1;
	        		//go left down
		            while(col >= 0 && row < n) {
		            	if(sol[row][col] == 2) break;
		            	if(sol[row][col] == 0) {
		            		sol[row][col] = 3;
		            	}
		            	col--;
		            	row++;
		            }
		        }

        	}
        	
        }
    }
    
    public static boolean validPosition(int row, int col, List<String> tree, int[][] positions) {
    	
    	if(tree.get(row).charAt(col) == '2') return false;
    	
        for(int pRow = positions.length-1; pRow >= 0; pRow--) {
        	for(int pCol = positions[pRow].length-1; pCol >= 0; pCol--) {
        		
        		if(positions[pRow][pCol] == 1) {
        			if(pRow == row && pCol == col) {
	        			return false;
	        		}
	        		if(pRow == row) {
		        		boolean isTree = false;
		        		int tCol = pCol;
		        		int inCol = col;
		        		if(inCol < tCol){
		        			int temp = inCol;
		        			inCol = tCol;
		        			tCol = temp;
		        		}
		        		for(int checkCol = inCol-1; checkCol > tCol; checkCol--) {
		        			if(tree.get(row).charAt(checkCol) == '2') {
		        				isTree = true;
		        			}
		        		}
		        		if(isTree == false) {
		        			return false;
		        		}
		        	}

	        		if(pCol == col) {
		        		boolean isTree = false;
		        		int inRow = row;
		        		int tRow = pRow;
		        		if(row < tRow) {
		        			int temp = inRow;
		        			inRow = tRow;
		        			tRow = temp;
		        		}
		        		for(int checkRow = inRow-1; checkRow > tRow; checkRow--) {
		        			if(tree.get(checkRow).charAt(col) == '2') {
		        				isTree = true;
		        			}
		        		}
		        		if(isTree == false) {
		        			return false;
		        		}
		        	}
		            if(pCol - col == (pRow - row) * -1) {
		            	int checkRow;
		            	int checkCol;
		            	int times;
		            	if(row > pRow) {
							checkRow = row - 1;
							checkCol = col + 1;
							times = row - pRow - 1;
		            	} else {
		            		checkRow = pRow - 1;
		            		checkCol = pCol + 1;
		            		times = pRow - row - 1;
		            	}

		                boolean isTree = false;
		                while(times > 0) {
		                	if(tree.get(checkRow).charAt(checkCol) == '2') {
		        				isTree = true;
		        			}
		                	checkCol++;
		                	checkRow--;
		                	times--;
		                }
		        		if(isTree == false) {
		        			return false;
		        		}
		            }
		            if(pCol - col == pRow - row) {
		            	int checkRow;
		            	int checkCol;
		            	int times;
		            	if(row > pRow) {
							checkRow = row - 1;
							checkCol = col - 1;
							times = row - pRow - 1;
		            	} else {
		            		checkRow = pRow - 1;
		            		checkCol = pCol - 1;
		            		times = pRow - row - 1;
		            	}

		                boolean isTree = false;
		                while(times > 0) {
		                	if(tree.get(checkRow).charAt(checkCol) == '2') {
		        				isTree = true;
		        			}
		                	checkCol--;
		                	checkRow--;
		                	times--;
		                }
		        		if(isTree == false) {
		        			return false;
		        		}
		            }

        		}
        	}
        	
        }
        return true;
    }

}



