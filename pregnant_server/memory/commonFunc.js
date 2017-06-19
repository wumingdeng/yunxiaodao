
var m = require('./memoryVarible')
var db = require('../models')
var mod = {
    ReloadMemory: function (tbl) {
        // reload all
        // configs,swipe_configs,services,catalogs,work_services
        db[tbl].findAll().then(function (data) {
            m[tbl] = {}
            for (var i = 0; i < data.length; i++) {
                m[tbl][data[i].id] = data[i].dataValues
            }
        })
    },
    ReloadWorkersMemory: function (id) {
        // reload single
        db.workers.findAll({ where: { id: id } }).then(function (data) {
            for (var i = 0; i < data.length; i++) {
                m.workers[data[i].id] = data[i].dataValues
            }
        })
    },
    ReloadCatServicesMemory: function () {
        db.cat_services.findAll().then(function (data6) {
            m.cat_services = {}
            for (var i = 0; i < data6.length; i++) {
                if (m.cat_services[data6[i].cat_id]) {
                    m.cat_services[data6[i].cat_id].push(data6[i].service_id)
                } else {
                    m.cat_services[data6[i].cat_id] = [data6[i].service_id]
                }
            }
        })
    },
    InitDbMemory: function () {
        db.swipe_configs.findAll().then(function (data2) {
            for (var i = 0; i < data2.length; i++) {
                m.swipe_configs[data2[i].id] = data2[i].dataValues
            }
            db.products.findAll().then(function (data3) {
                for (var i = 0; i < data3.length; i++) {
                    m.products[data3[i].id] = data3[i].dataValues
                }
                db.weightAdvice_configs.findAll().then(function (data4) {
                    for (var i = 0; i < data4.length; i++) {
                        m.weightAdvice_configs[data4[i].id] = data4[i].dataValues
                    }
                    db.weightRate_configs.findAll().then(function (data5) {
                        for (var i = 0; i < data5.length; i++) {
                            m.weightRate_configs[data5[i].id] = data5[i].dataValues
                        }
                        db.weight_diet_configs.findAll().then(function (data6) {
                            for (var i = 0; i < data6.length; i++) {
                                m.weight_diet_configs[data6[i].week] = data6[i].dataValues
                            }
                            db.footknowledge_configs.findAll().then(function(data7) {
                                for(var i = 0;i<data7.length;i++){
                                    m.footknowledge_configs[data7[i].id] = data7[i].dataValues
                                }
                                db.footType_advice_configs.findAll().then(function(data8) {
                                    for(var i = 0;i<data8.length;i++){
                                        m.footType_advice_configs[data8[i].id] = data8[i].dataValues
                                    }   
                                    db.check_cycle_configs.findAll().then(function(data9) {
                                        for(var i = 0;i<data9.length;i++){
                                            m.check_cycle_configs[data9[i].id] = data9[i].dataValues
                                        }   
                                    })                                
                                })
                            })
                        })
                    })
                })
            })
        })
    },
    reloadWeightConfig: function () {
        db.weightAdvice_configs.findAll().then(function (data4) {
            for (var i = 0; i < data4.length; i++) {
                m.weightAdvice_configs[data4[i].id] = data4[i].dataValues
            }
            db.weight_diet_configs.findAll().then(function (data6) {
                for (var i = 0; i < data6.length; i++) {
                    m.weight_diet_configs[data6[i].week] = data6[i].dataValues
                }
            })
        })
    }
}

module.exports = mod