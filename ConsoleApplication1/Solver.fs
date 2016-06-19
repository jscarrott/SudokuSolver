// Learn more about F# at http://fsharp.org
// See the 'F# Tutorial' project for more help.

module SudukoSolver

open Microsoft.FSharp.Core.Operators
open SudokuCell
open SudokuGrid

let rec cellListToMap (list : List<Cell>, cellMap : Map<string, Cell>) =
    if (list.IsEmpty) then cellMap
    else
        let appendedCellMap = cellMap.Add(ID list.Head, list.Head)
        cellListToMap (list.Tail, appendedCellMap)

let printGrid (grid : Grid) =
    printf "Grid: "
    printfn ""
    let cellMap = cellListToMap (grid.Cells, Map.empty)
    for i = 1 to 9 do
        for j = 1 to 9 do
            let ID = i.ToString() + j.ToString()
            let cell = cellMap.[ID]
            if (Solved cell) then printf "%i " cell.possibleValues.[0]
            else printf "0 "
        printfn ""

let printCell (cell : Cell) =
    printfn "Row %i Group %i , Column %i, Group %i, Possible Values %A" cell.row (rowGroup cell) cell.column
        (columnGroup cell) cell.possibleValues

let printGrids (grids : List<Grid>) =
    printfn "Printing Grids"
    for grid in grids do
        printGrid grid

let ReadInPuzzle = System.IO.File.ReadAllLines(".\Puzzle.txt")

let parseLines =
    ReadInPuzzle
    |> Array.map (fun x -> x.Split ',')
    |> Array.toList

let parseCells =
    [ for line in parseLines do
            yield Array.toList line ]

let parseCellValue value =
    if (int value > 0) then [ int value ]
    else [ 1..9 ]

let startingGrid =
    Grid(parseCells.Length, parseCells.[0].Length,
            [ for i = 0 to parseCells.Length - 1 do
                for j = 0 to parseCells.[0].Length - 1 do
                    yield {row = i + 1; column = j + 1; possibleValues = parseCellValue parseCells.[i].[j]} ], 1)

//            printfn "this cell"
//            printCell cell
//            printfn "Compared to"
//            printCell cells.Head
let rec trySolveValues (cell : Cell) cells =
    match cells with
    | [] -> cell
    | head :: tail ->
        if checkConflicts cell head then
            trySolveValues
                ({
                        row = cell.row;
                        column = cell.column;
                        possibleValues = cell.possibleValues |> List.filter (fun x -> x <> head.possibleValues.[0])
                        }) tail
        else trySolveValues cell tail

let rec solvePassOnAllCells cells solvedCells checkedCells =
    match cells with
    | [] -> checkedCells |> List.append solvedCells
    | head :: tail ->
        let appendedCheckedCells =
            trySolveValues head (solvedCells |> List.filter (fun x -> isDifferent head x)) :: checkedCells
        solvePassOnAllCells tail solvedCells appendedCheckedCells

let rec checkAllGrids (uncheckedGrids : Grid list) checkedGrids =
    match uncheckedGrids with
    | [] -> checkedGrids
    | head :: tail ->
        let appendedCheckedGrids =
            Grid(9, 9, solvePassOnAllCells head.unsolvedCells head.solvedCells [], head.Level + 1) :: checkedGrids
        checkAllGrids uncheckedGrids.Tail appendedCheckedGrids

let rec solvePuzzle input : List<Grid> =
    let finishedGrids = (input |> List.filter (fun (x : Grid) -> x.isFinished))
    let solved = finishedGrids.Length > 0
    match solved with
    | true -> finishedGrids
    | false ->
        let maxLevel =
            input
            |> List.map (fun x -> x.Level)
            |> List.max

        let gridsToBeChecked = input |> List.filter (fun x -> x.Level = maxLevel)
        let appendedGrids = input |> List.append (checkAllGrids gridsToBeChecked [])
        solvePuzzle (appendedGrids)

printfn "Input Puzzle"
printfn ""
printGrid startingGrid

let grids = startingGrid :: []
let finishedGrids = lazy (solvePuzzle grids)

let printSolution someGrids =
    for grid in someGrids do
        let index = someGrids |> List.tryFindIndex (fun x -> x = grid)
        printfn "Solution Number: %A" (index.Value + 1)
        printGrid grid

