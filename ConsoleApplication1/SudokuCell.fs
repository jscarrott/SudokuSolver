module SudokuCell

type Cell =
    { row : int
      column : int
      possibleValues : int list }

type CellsToCompare = { cell : Cell; otherCell : Cell}

type Result<'TSuccess,'TFailure> = 
    | Success of 'TSuccess
    | Failure of 'TFailure

let bind comparisonFunction = 
    fun cellComparisonFunction -> 
        match cellComparisonFunction with
        | Success s -> Success s
        | Failure f -> comparisonFunction f

let (>>=) cellComparisonFunction comparisonFunction = 
    bind comparisonFunction cellComparisonFunction 

let ResultToBool res =
    match res with
    | Success x -> true
    | Failure() -> false

let roundUpForGrouping (x : float) = System.Math.Ceiling x
let columnGroup cell               = int (roundUpForGrouping (float (cell.column) / 3.0))
let rowGroup cell                  = int (roundUpForGrouping (float (cell.row) / 3.0))
let Solved cell                    = cell.possibleValues.Length = 1
let ID cell                        = (cell.column, cell.row)

let isDifferent cells =
    if((cells.cell.column = cells.otherCell.column) && (cells.cell.row = cells.otherCell.row)) then Success cells
    else Failure cells

let columnMatch cells = 
    if(cells.cell.column = cells.otherCell.column) then Success cells
    else Failure cells

let rowMatch cells = 
    if(cells.cell.row = cells.otherCell.row) then Success cells
    else Failure cells

let groupMatch cells = 
    if(rowGroup cells.cell = rowGroup cells.otherCell) && (columnGroup cells.cell = columnGroup cells.otherCell) then Success cells
    else Failure cells

let valueConflict cells = 
    (cells.cell.possibleValues |> List.exists (fun x -> x = cells.otherCell.possibleValues.[0]))

let checkConflicts cell otherCell =
    let result = 
            {cell = cell; otherCell = otherCell}
            |> isDifferent
            >>= columnMatch
            >>= rowMatch
            >>= groupMatch

    match result with
    | Success x -> valueConflict x
    | Failure x -> false





//let checkConflicts (cell : Cell) (otherCell : Cell) = 
//    ((otherCell.column = cell.column) || (otherCell.row = cell.row) || ((rowGroup otherCell = rowGroup cell) && (columnGroup otherCell = columnGroup cell))) && (cell.possibleValues |> List.exists (fun x -> x = otherCell.possibleValues.[0]))
