open SudukoSolver

[<EntryPoint>]
let main argv =
    let start = System.DateTime.Now
    printSolution (finishedGrids.Force())
    let endTime = System.DateTime.Now
    let span = endTime.Subtract(start).ToString()
    printf "%s" span
    System.Console.Read() |> ignore
    0 // return an integer exit code