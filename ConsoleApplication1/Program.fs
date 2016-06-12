// Learn more about F# at http://fsharp.org
// See the 'F# Tutorial' project for more help.

module Solver =

    let start = System.DateTime.Now

    open Microsoft.FSharp.Core.Operators

    let rec remove_first pred lst =
      match lst with
      | h::t when pred h -> t
      | h::t -> h::remove_first pred t
      | _ -> []

    type Cell(row : int, column: int, possValues : List<int>) as self =

        let roundUpForGrouping (x:float) =         
            System.Math.Ceiling x
        
        member this.Row = row

        member this.Column = column

        member this.ID = this.Column.ToString() + this.Row.ToString()

        member this.RowGroup = int (roundUpForGrouping( float(this.Row) / 3.0))

        member this.ColumnGroup = int(roundUpForGrouping ( float(this.Column) / 3.0))

        member this.PossibleValues = possValues

        member this.Solved = this.PossibleValues.Length = 1

        member this.IsSame (otherCell:Cell) = ((otherCell.Column = self.Column) && (otherCell.Row = self.Row))

        member this.IsDifferent (otherCell:Cell) = not (self.IsSame otherCell)

        member this.checkConflicts (otherCell:Cell) = ((otherCell.Column = self.Column) || (otherCell.Row = self.Row) || ((otherCell.RowGroup = self.RowGroup) && (otherCell.ColumnGroup = self.ColumnGroup))) && (self.PossibleValues |> List.exists (fun x -> x = otherCell.PossibleValues.[0]))
         

    let testCell = Cell(5, 8, [1;2;3;4])

    type Grid(numberOfRows : int, numberOfColumns : int,cells : List<Cell>, level : int) as self =

        member this.solvedCells = 
            self.Cells |> List.filter (fun (x:Cell) -> x.Solved)

        member this.unsolvedCells =
            self.Cells |> List.filter (fun (x:Cell) -> not x.Solved)

        member this.isFinished = this.unsolvedCells.Length = 0

        member this.Level = level

        member this.Rows = [1 .. numberOfRows]

        member this.Columns = [1 .. numberOfColumns]

        member this.Cells = 
            if (cells = List.Empty) then 
                                  Seq.toList(seq { for i in this.Rows do
                                                     for j in this.Columns do
                                                         yield Cell(i, j, [1 ..9])})

            else cells



    let rec cellListToMap (list:List<Cell>, cellMap:Map<string, Cell>) =
        if(list.IsEmpty) then cellMap
        else 
            let appendedCellMap = cellMap.Add(list.Head.ID, list.Head)
            cellListToMap(list.Tail, appendedCellMap)

    let printGrid (grid:Grid) =
        printf "Grid: "
        printfn ""
        let cellMap = cellListToMap(grid.Cells, Map.empty)
        for i = 1 to 9 do
            for j = 1 to 9 do
                let ID = i.ToString() + j.ToString()
                let cell = cellMap.[ID]
                if(cell.Solved) then 
                    printf "%i " cell.PossibleValues.[0]
                else printf "0 "
            printfn ""


    let printCell (cell:Cell) =
        printfn"Row %i Group %i , Column %i, Group %i, Possible Values %A" cell.Row cell.RowGroup cell.Column cell.ColumnGroup cell.PossibleValues

    let printGrids (grids:List<Grid>) = 
        printfn "Printing Grids"
        for grid in grids
            do printGrid grid

                
                
    let ReadInPuzzle = System.IO.File.ReadAllLines(".\Puzzle.txt")

    let parseLines = 
        ReadInPuzzle 
        |> Array.map (fun x -> x.Split ',')
        |> Array.toList

    let parseCells =
        [for line in parseLines do
            yield Array.toList line]

    let parseCellValue value =
        if (int value > 0) then [int value]
        else [1..9]

    let startingGrid = 
       Grid(parseCells.Length, parseCells.[0].Length, [for i = 0 to parseCells.Length - 1 do
                                                        for j = 0 to parseCells.[0].Length - 1 do
                                                            yield Cell(i + 1,j + 1, parseCellValue parseCells.[i].[j])], 1)

    

//            printfn "this cell"
//            printCell cell
//            printfn "Compared to"
//            printCell cells.Head

    let rec trySolveValues (cell:Cell, cells:List<Cell> ) =
        if(cells.IsEmpty) then cell
        else
            if(cell.checkConflicts cells.Head)
            then trySolveValues(Cell(cell.Row, cell.Column, cell.PossibleValues |> List.filter (fun x -> x <> cells.Head.PossibleValues.[0])), cells.Tail)
            else trySolveValues(cell, cells.Tail)
    
    let rec solvePassOnAllCells (cells:List<Cell>, solvedCells:List<Cell>, checkedCells:List<Cell>) =
        if(cells.IsEmpty)
         then checkedCells |> List.append solvedCells
        else 
            let appendedCheckedCells = trySolveValues(cells.Head, solvedCells |> List.filter(fun x -> cells.Head.IsDifferent x)) :: checkedCells
            solvePassOnAllCells(cells.Tail, solvedCells, appendedCheckedCells)

    let rec checkAllGrids (uncheckedGrids:List<Grid>, checkedGrids:List<Grid>) =
        if(uncheckedGrids.IsEmpty) then checkedGrids 
        else
            let appendedCheckedGrids = Grid(9 ,9, solvePassOnAllCells(uncheckedGrids.Head.unsolvedCells, uncheckedGrids.Head.solvedCells, []), uncheckedGrids.Head.Level + 1) :: checkedGrids
            checkAllGrids(uncheckedGrids.Tail, appendedCheckedGrids)

    let rec solvePuzzle input:List<Grid> =
        printGrids input
        let finishedGrids = (input |> List.filter(fun (x:Grid) -> x.isFinished))
        let solved = finishedGrids.Length > 0
        if(solved) then finishedGrids
        else
            let appendedGrids = input |> List.append(checkAllGrids(input, []))
            solvePuzzle(appendedGrids)
            
                


    printfn "Grid"
    printfn ""
    printGrid startingGrid

//    printfn "Rows:"
//    printfn ""
//    printRow startingGrid
//
//    printfn "Columns:"
//    printfn ""
//    printColumn startingGrid
//
//    printfn "Column Groups:"
//    printfn ""
//    printColumnGroups startingGrid
//
//    printfn "Row Groups:"
//    printfn ""
//    printRowGroups startingGrid

//Simple solve method for a sudoku no support for when there are logical guesses outside the simple solving framework
//    while isFinished startingGrid do
//    
//        printfn "Number of solved cells %i" (solvedCells startingGrid).Length
//
//        printGrid startingGrid
//    
//        for cell in unsolvedCells startingGrid do
//            checkCellPossibleValues (cell, startingGrid)

    let grids = startingGrid :: []

    
    let finishedGrids = solvePuzzle grids

    for grid in finishedGrids
        do printGrid grid

//    printGrid startingGrid

    let endTime = System.DateTime.Now

    let span = endTime.Subtract(start).ToString()

    printf "%s" span

    System.Console.Read() |> ignore

[<EntryPoint>]
let main argv = 
    0 // return an integer exit code


