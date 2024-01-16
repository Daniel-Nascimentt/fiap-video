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




var video1 = {
    titulo: "Call of Duty Warzone",
    url: "http://warzone-url.com",
    dataPublicacao: new Date(),
    performance: {
        visualizacoes: 500,
        marcadoFavorito: 245
    },
    categoria: "Jogos",
    publicadoPor: {
        $ref: "usuarios",
        $id: usuariosInseridos.insertedIds[0]
    }
}

var video2 = {
    titulo: "Call of Duty MW",
    url: "http://MW-url.com",
    dataPublicacao: new Date(),
    performance: {
        visualizacoes: 345,
        marcadoFavorito: 214
    },
    categoria: "Jogos",
    publicadoPor: {
        $ref: "usuarios",
        $id: usuariosInseridos.insertedIds[1]
    }
}

var video3 = {
    titulo: "Spring boot com Java",
    url: "http://programacao-url.com",
    dataPublicacao: new Date(),
    performance: {
        visualizacoes: 123,
        marcadoFavorito: 111
    },
    categoria: "programacao",
    publicadoPor: {
        $ref: "usuarios",
        $id: usuariosInseridos.insertedIds[2]
    }
}

var video4 = {
    titulo: "Call of Duty MW3",
    url: "http://MW3-url.com",
    dataPublicacao: new Date(),
    performance: {
        visualizacoes: 456,
        marcadoFavorito: 223
    },
    categoria: "Jogos",
    publicadoPor: {
        $ref: "usuarios",
        $id: usuariosInseridos.insertedIds[2]
    }
}


var video5 = {
    titulo: "Call of Duty BO2",
    url: "http://bo2-url.com",
    dataPublicacao: new Date(),
    performance: {
        visualizacoes: 998,
        marcadoFavorito: 567
    },
    categoria: "Jogos",
    publicadoPor: {
        $ref: "usuarios",
        $id: usuariosInseridos.insertedIds[0]
    }
}

var video6 = {
    titulo: "Clean Code",
    url: "http://clean-code-url.com",
    dataPublicacao: new Date(),
    performance: {
        visualizacoes: 776,
        marcadoFavorito: 491
    },
    categoria: "programacao",
    publicadoPor: {
        $ref: "usuarios",
        $id: usuariosInseridos.insertedIds[1]
    }
}

var video7 = {
    titulo: "PostgreSQL",
    url: "http://postgres-url.com",
    dataPublicacao: new Date(),
    performance: {
        visualizacoes: 445,
        marcadoFavorito: 223
    },
    categoria: "programacao",
    publicadoPor: {
        $ref: "usuarios",
        $id: usuariosInseridos.insertedIds[2]
    }
}

var video8 = {
    titulo: "Call of Duty BO3",
    url: "http://MW3-url.com",
    dataPublicacao: new Date(),
    performance: {
        visualizacoes: 0,
        marcadoFavorito: 0
    },
    categoria: "Jogos",
    publicadoPor: {
        $ref: "usuarios",
        $id: usuariosInseridos.insertedIds[2]
    }
}


var videosInseridos = db.videos.insertMany([video1, video2, video3, video4, video5, video6, video7, video8])


db.contas.update(
    { "_id": contasInseridas.insertedIds[0] },
    {
        $push: {
            "videosPublicados": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[0] }, { $ref: "videos", $id: videosInseridos.insertedIds[4] }]},
            "videosAssistidos": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[1] }, { $ref: "videos", $id: videosInseridos.insertedIds[2]}] },
            "favoritos": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[2]}]}
        }
    }
);

db.contas.update(
    { "_id": contasInseridas.insertedIds[1] },
    {
        $push: {
            "videosPublicados": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[1] }, { $ref: "videos", $id: videosInseridos.insertedIds[5] }]},
            "videosAssistidos": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[0] }, { $ref: "videos", $id: videosInseridos.insertedIds[2]}] },
            "favoritos": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[0]}]}
        }
    }
);

db.contas.update(
    { "_id": contasInseridas.insertedIds[2] },
    {
        $push: {
            "videosPublicados": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[2] }, { $ref: "videos", $id: videosInseridos.insertedIds[3] }, { $ref: "videos", $id: videosInseridos.insertedIds[6] }]},
            "videosAssistidos": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[0] }, { $ref: "videos", $id: videosInseridos.insertedIds[1]}] },
            "favoritos": { $each: [{ $ref: "videos", $id: videosInseridos.insertedIds[1]}]}
        }
    }
);