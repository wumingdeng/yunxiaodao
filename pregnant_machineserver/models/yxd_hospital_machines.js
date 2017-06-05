'use strict';
module.exports = function(sequelize, DataTypes) {
  var yxd_machines = sequelize.define('yxd_machines', {
    hospital_no: DataTypes.STRING,
    machine_no: DataTypes.STRING,
    machine_mac:DataTypes.STRING,
    machine_type:DataTypes.STRING,
    machine_site:DataTypes.STRING,
    machine_ip:DataTypes.STRING,
    date:DataTypes.STRING,
    status:DataTypes.STRING,
    info:DataTypes.STRING
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return yxd_machines;
};