module SudokuCell

type Cell =
    { row : int
      column : int
      possibleValues : int list }

let roundUpForGrouping (x : float)                     = System.Math.Ceiling x
let columnGroup (cell : Cell)                          = int (roundUpForGrouping (float (cell.column) / 3.0))
let rowGroup (cell : Cell)                             = int (roundUpForGrouping (float (cell.row) / 3.0))
let Solved(cell : Cell)                                = cell.possibleValues.Length = 1
let ID(cell : Cell)                                    = cell.column.ToString() + cell.row.ToString()
let isTheSame (cell : Cell) (otherCell : Cell)         = (cell.column = otherCell.column) && (cell.row = otherCell.row)
let isDifferent (cell : Cell) (otherCell : Cell)       = not (isTheSame cell otherCell)
let checkConflicts (cell : Cell) (otherCell : Cell)    =
    ((otherCell.column                                 = cell.column) || (otherCell.row = cell.row)
     || ((rowGroup otherCell                           = rowGroup cell) && (columnGroup otherCell = columnGroup cell)))
    && (cell.possibleValues |> List.exists (fun x -> x = otherCell.possibleValues.[0]))
