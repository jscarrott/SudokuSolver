// Learn more about F# at http://fsharp.org
// See the 'F# Tutorial' project for more help.
module SudukoSolver

open Microsoft.FSharp.Core.Operators
open SudokuCell
open SudokuGrid
open GridPrinter
open GridParser

let rec trySolveValues (cell : Cell) cells = 
    match cells with
    | [] -> cell
    | head :: tail -> 
        if checkConflicts cell head then 
            trySolveValues ({ row = cell.row
                              column = cell.column
                              possibleValues = cell.possibleValues |> List.filter (fun x -> x <> head.possibleValues.[0]) }) tail
        else trySolveValues cell tail

let rec solvePassOnAllCells cells solvedCells checkedCells = 
    match cells with
    | [] -> checkedCells |> List.append solvedCells
    | head :: tail -> 
        let appendedCheckedCells = trySolveValues head (solvedCells |> List.filter (fun x -> isDifferent head x)) :: checkedCells
        solvePassOnAllCells tail solvedCells appendedCheckedCells

let rec checkAllGrids (uncheckedGrids : Grid list) checkedGrids = 
    match uncheckedGrids with
    | [] -> checkedGrids
    | head :: tail -> 
        let unsolved = UnsolvedCells head.cells
        let solved = SolvedCells head.cells
        let appendedCheckedGrids = {numberOfRows = 9; numberOfColumns = 9; cells = solvePassOnAllCells unsolved solved []; level = head.level + 1} :: checkedGrids
        checkAllGrids uncheckedGrids.Tail appendedCheckedGrids

let rec solvePuzzle (input : Grid list) = 
    let finishedGrids = input |> List.filter(IsGridFinished) 
    let solved = finishedGrids.Length > 0
    match solved with
    | true -> finishedGrids
    | false -> 
        let maxLevel = 
            input
            |> List.map (fun x -> x.level)
            |> List.max
        
        let gridsToBeChecked = input |> List.filter (fun x -> x.level = maxLevel)
        let appendedGrids = input |> List.append (checkAllGrids gridsToBeChecked [])
        solvePuzzle (appendedGrids)

printfn "Input Puzzle"
printfn ""
printGrid startingGrid

let grids = [ startingGrid ]
let finishedGrids = lazy (solvePuzzle grids)

let printSolution someGrids = 
    for grid in someGrids do
        let index = someGrids |> List.tryFindIndex (fun x -> x = grid)
        printfn "Solution Number: %A" (index.Value + 1)
        printGrid grid
