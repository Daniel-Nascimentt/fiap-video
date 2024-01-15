var db = db.getSiblingDB("test");

var conta1 = {
    videosPublicados: [],
    videosAssistidos: [],
    favoritos: []
};
var conta2 = {
    videosPublicados: [],
    videosAssistidos: [],
    favoritos: []
};
var conta3 = {
    videosPublicados: [],
    videosAssistidos: [],
    favoritos: []
};

var contasInseridas = db.contas.insertMany([conta1, conta2, conta3]);
print("Contas inseridas:", contasInseridas.insertedIds);

var usuario1 = {
    nome: "Usuário 1",
    email: "usuario1@example.com",
    dataNascimento: new Date("1990-01-01"),
    conta: {
        $ref: "contas",
        $id: contasInseridas.insertedIds[0]
    },
    cadastradoEm: new Date()
};
var usuario2 = {
    nome: "Usuário 2",
    email: "usuario2@example.com",
    dataNascimento: new Date("1995-02-15"),
    conta: {
        $ref: "contas",
        $id: contasInseridas.insertedIds[1]
    },
    cadastradoEm: new Date()
};
var usuario3 = {
    nome: "Usuário 3",
    email: "usuario3@example.com",
    dataNascimento: new Date("1988-07-10"),
    conta: {
        $ref: "contas",
        $id: contasInseridas.insertedIds[2]
    },
    cadastradoEm: new Date()
};

var usuariosInseridos = db.usuarios.insertMany([usuario1, usuario2, usuario3]);