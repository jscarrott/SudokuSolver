module SudokuGrid

open SudokuCell

type Grid = 
        {numberOfRows : int; numberOfColumns : int; cells : Cell list; level : int;}


let SolvedCells cells     = cells |> List.filter (Solved)
let UnsolvedCells cells   = cells |> List.filter (Solved  >> not)
let IsGridFinished grid   = UnsolvedCells(grid.cells).Length = 0

let Cells grid =
    if (grid.cells = List.Empty) then
        Seq.toList (seq {
                        for i = 1 to grid.numberOfRows + 1 do
                            for j = 1 to grid.numberOfColumns + 1 do
                                yield { row = i
                                        column = j
                                        possibleValues = [ 1..9 ] } //How cool is this type inference!
                    })
    else grid.cells

