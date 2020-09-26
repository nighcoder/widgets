
########################################################################################
#  ARGUMENTS
########################################################################################

Param (
    [Alias('i')][string] $identity,
    [Parameter(Mandatory=$true, Position=0)][string] $kernel
)

########################################################################################
#  DEFAULTS
########################################################################################

if (!$identity) {
    $identity = "widgets-$(bin/version)"
}

$UserLibPath = "$env:LocalAppData\Programs"
$SystemLibPath = "$env:ProgramFiles"

if (Test-Path -PathType Container "$SystemLibPath\$kernel") {
    $LibDir = Get-ChildItem "$SystemLibPath\$kernel"
}

if (Test-Path -PathType Container "$UserLibPath\$kernel") {
    $LibDir = Get-ChildItem "$UserLibPath\$kernel"
}

########################################################################################
#  FAIL CONDITIONS
########################################################################################

if (!$LibDir) {
    Write-Error "Can't find kernel $kernel"
    exit 10
}

if (! $(Test-Path -PathType Leaf "$LibDir\plugins\$identity.jar")) {
    Write-Error "Can't find $identity installed under $kernel"
    exit 20
}

########################################################################################
#  DEPENDENCIES
########################################################################################

function Get-Dependencies {
    Param (
        $jar
    )
    $jarfile = [System.IO.Compression.ZipFile]::OpenRead($jar.FullName)
    $manifest = $jarfile |
                Select-Object -ExpandProperty Entries |
                Where-Object {$_.Name -eq "MANIFEST.MF"}
    $file = New-TemporaryFile
    $file.Delete()
    [System.IO.Compression.ZipFileExtensions]::ExtractToFile($manifest, $file.FullName)
    $jarfile.Dispose()
    $CP_key = $false
    $deps = foreach ($line in $(Get-Content $file)) {
                Switch -Regex ($line) {
                    "^Class-Path: (.+)$" {
                        $CP_key = $true
                        Write-Output $matches[1]
                    }
                    "^[^:]+$" {
                        if ($CP_key) {
                                Write-Output $line.Substring(1)
                            }
                    }
                    default {
                        $CP_key = $false
                    }
                }
            }
    foreach ($dep in $($deps -join '').split(" ")) {
        $file = Get-ChildItem "$($jar.Directory)\$dep"
        if ($file.target) {
            Get-ChildItem $file.target
        }
        else {
            $file
        }
    }
}

$deps = Get-Dependencies $(Get-ChildItem "$LibDir\plugins\$identity.jar")

$other_deps = @()
foreach ($jar in $(Get-ChildItem -Exclude "$identity.jar" "$LibDir\$kernel.jar", "$LibDir\plugins\*")) {
    $other_deps += Get-Dependencies $jar
}

Write-Host $deps
Write-Host $other_deps

exit 120

########################################################################################
#  UNINSTALLING
########################################################################################

Remove-Item "$LibDir\plugins\$identity.jar"
Remove-Item "$LibDir\plugins\enabled\$identity.jar" | Out-Null
Remove-Item "$LibDir\lib\$identity"
