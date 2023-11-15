### Prerequisites:
- Need to have Docker (Docker Compose) installed.

### Instructions:
- Clone/download from [lpost-airports at github](https://github.com/AurJur/lpost-airports)
- Execute:
    - docker compose -f ./docker-compose.yml up  
      (where ***./docker-compose.yml*** is the file in this project, so change the path accordingly if executing from a different directory)
- Takes some time to start up (5min+), but should work out of the box. Tested on Linux (Ubuntu, Debian). If not - I'd be glad to know that. 

### Notes:
- React frontend should be available at localhost:3000
- Files for import via frontend are in this project: input-data-csv directory.