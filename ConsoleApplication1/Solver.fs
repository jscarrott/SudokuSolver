// Learn more about F# at http://fsharp.org
// See the 'F# Tutorial' project for more help.
module SudukoSolver

open Microsoft.FSharp.Core.Operators
open SudokuCell
open SudokuGrid
open GridPrinter
open GridParser


let rec trySolveValues cell (cells : Cell list) =    
    if cells.IsEmpty then cell
    else
        match checkConflicts cell cells.Head  with
        | true -> {cell with possibleValues  = cell.possibleValues |> List.filter (fun x -> x <> cells.Head.possibleValues.[0])}
        | false -> cell
        |> (fun cell -> trySolveValues cell cells.Tail)


let solvePassOnAllCells unsolvedCells solvedCells = 
    unsolvedCells |> List.fold (fun acc elem-> (trySolveValues elem solvedCells) :: acc) [] |> List.append solvedCells


let checkAllGrids uncheckedGrids =
    uncheckedGrids |> List.fold ( fun acc elem -> 
        let unsolved = UnsolvedCells elem.cells
        let solved = SolvedCells elem.cells
        {elem with cells = solvePassOnAllCells unsolved solved; level = elem.level + 1} :: acc) []

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
        let appendedGrids = input |> List.append (checkAllGrids gridsToBeChecked)
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
