Select-String -Pattern '\(defproject widgets "(.+)"' -Path project.clj | % { $_.Matches.Groups[1].Value }
