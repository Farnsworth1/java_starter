REM java -classpath C:\Users\m_albezem\sciebo\zzz_AUSBILDUNG_Maher\IHK\Vorlage\out\production\Vorlage Main -i C:\Users\m_albezem\sciebo\zzz_AUSBILDUNG_Maher\IHK\Vorlage\tests\E001.txt

echo off
setlocal enableDelayedExpansion
set /a ID=1

for /f "delims=" %%G in ('dir tests\E???.txt /s/b') do (
   set testcases[!ID!]=%%~G
   set /a ID+=1
)

REM set testcases > null

for /F "tokens=2 delims==" %%s in ('set testcases') do (
    java -jar out\artifacts\Vorlage\Vorlage.jar -i %%s
)

endlocal
