para publicar em produção utilize o comando

scp -r .\target\InfinitySystem-1.jar ubuntu@54.207.211.93:/home/ubuntu/infinity-group/api/

nota lembre se de alterar a versão do jar.

No servidor para parar o serviço utilize os comandos

lsof -i tcp:8080 -> descobre se existe algum serviço utilizando a porta 8080

kill [id do processo] para matar o processo