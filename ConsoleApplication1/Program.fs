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

    type Cell(row : int, column: int, possValues : List<int>) =

        let roundUpForGrouping (x:float) =         
            System.Math.Ceiling x
        
        member this.Row = row

        member this.Column = column

        member this.RowGroup = int (roundUpForGrouping( float(this.Row) / 3.0))

        member this.ColumnGroup = int(roundUpForGrouping ( float(this.Column) / 3.0))

        member val PossibleValues = possValues with get, set

        member this.Solved = this.PossibleValues.Length = 1
         

    let testCell = Cell(5, 8, [1;2;3;4])

    type Grid(numberOfRows : int, numberOfColumns : int,cells : List<Cell>) =


        member this.Rows = [1 .. numberOfRows]

        member this.Columns = [1 .. numberOfColumns]

        member this.Cells = 
            if (cells = List.Empty) then 
                                  Seq.toList(seq { for i in this.Rows do
                                                     for j in this.Columns do
                                                         yield Cell(i, j, [1 ..9])})

            else cells

        

    let printGrid (grid:Grid) =
        for i = 0 to 80 do
            if(grid.Cells.[i].Solved) then
                printf "%i " grid.Cells.[i].PossibleValues.[0]
            else printf "0 "
            if( (i+1) % 9 = 0) then printfn ""

            
    let printRowGroups (grid:Grid) =
        for i = 0 to 80 do
            printf "%i " grid.Cells.[i].RowGroup
            if( (i+1) % 9 = 0) then printfn ""



    let printColumnGroups (grid:Grid) =
        for i = 0 to 80 do
            printf "%i " grid.Cells.[i].ColumnGroup
            if( (i+1) % 9 = 0) then printfn ""

    let printColumn (grid:Grid) =
        for i = 0 to 80 do
            printf "%i " grid.Cells.[i].Column
            if( (i+1) % 9 = 0) then printfn ""


    let printRow (grid:Grid) =
        for i = 0 to 80 do
            printf "%i " grid.Cells.[i].Row
            if( (i+1) % 9 = 0) then printfn ""

                
                
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
                                                            yield Cell(i + 1,j + 1, parseCellValue parseCells.[i].[j])])

    let solvedCells (grid : Grid) = 
        grid.Cells |> List.filter (fun x -> x.Solved)

    let unsolvedCells (grid: Grid) =
        grid.Cells |> List.filter (fun x -> not x.Solved)

    let isFinished grid = (unsolvedCells grid).Length > 0

    let checkCellPossibleValues (cell:Cell, grid:Grid ) =
        for gridCell in solvedCells grid do
            if(((gridCell.Column = cell.Column) || (gridCell.Row = cell.Row) || ((gridCell.RowGroup = cell.RowGroup)
                && (gridCell.ColumnGroup = cell.ColumnGroup))) && (cell.PossibleValues |> List.exists (fun x -> x = gridCell.PossibleValues.[0]))) 
                 then
//                 printfn "Row %i, Column %i removing value %i" cell.Row cell.Column gridCell.PossibleValues.[0]
                 cell.PossibleValues <- remove_first (fun x -> x = gridCell.PossibleValues.[0]) cell.PossibleValues 
        
                


    printfn "Grid"
    printfn ""
    printGrid startingGrid

    printfn "Rows:"
    printfn ""
    printRow startingGrid

    printfn "Columns:"
    printfn ""
    printColumn startingGrid

    printfn "Column Groups:"
    printfn ""
    printColumnGroups startingGrid

    printfn "Row Groups:"
    printfn ""
    printRowGroups startingGrid


    while isFinished startingGrid do
    
        printfn "Number of solved cells %i" (solvedCells startingGrid).Length

//        printGrid startingGrid
    
        for cell in unsolvedCells startingGrid do
            checkCellPossibleValues (cell, startingGrid)

    printGrid startingGrid

    let endTime = System.DateTime.Now

    let span = endTime.Subtract(start).ToString()

    printf "%s" span

[<EntryPoint>]
let main argv = 
    0 // return an integer exit code


