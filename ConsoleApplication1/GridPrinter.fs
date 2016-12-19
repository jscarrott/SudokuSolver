module GridPrinter

open SudokuGrid
open SudokuCell

let rec cellListToMap (list : Cell list, cellMap : Map<int*int, Cell>) =
    if (list.IsEmpty) then cellMap
    else
        let appendedCellMap = cellMap.Add(ID list.Head, list.Head)
        cellListToMap (list.Tail, appendedCellMap)

let printGrid (grid : Grid) =
    printf "Grid: "
    printfn ""
    let cellMap = cellListToMap (grid.cells, Map.empty)
    for i = 1 to 9 do
        for j = 1 to 9 do
            let cell = cellMap.[(i,j)]
            if (Solved cell) then printf "%i " cell.possibleValues.[0]
            else printf "0 "
        printfn ""

let printCell (cell : Cell) =
    printfn "Row %i Group %i , Column %i, Group %i, Possible Values %A" cell.row (rowGroup cell) cell.column
        (columnGroup cell) cell.possibleValues

let printGrids (grids : Grid List) =
    printfn "Printing Grids"
    for grid in grids do
        printGrid grid