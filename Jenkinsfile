@Library('libpipelines@master') _

hose {
    EMAIL = 'qa'
    LANG = 'java'
    MODULE = 'bdt'
    REPOSITORY = 'github.com/bdt'
    SLACKTEAM = 'stratioqa'
    DEVTIMEOUT = 30
    RELEASETIMEOUT = 30
    MAXITRETRIES = 2
    FOSS = true
    BUILDTOOL = 'maven'
    BUILDTOOLVERSION = '3.5.0'

    DEV = { config ->        
        doCompile(config)
     }
}
