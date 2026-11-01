import React, { useState, useMemo } from 'react';
import { Ship, Plus, Search, Edit, Trash2, X, Check, Filter } from 'lucide-react';
import { api } from '../services/api';

export function ShipsList({ navires, onRefresh }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [typeFilter, setTypeFilter] = useState('ALL');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingNavire, setEditingNavire] = useState(null);

  // Form Fields
  const [nom, setNom] = useState('');
  const [tjb, setTjb] = useState('');
  const [typeNavire, setTypeNavire] = useState('AUTRE');
  const [longueur, setLongueur] = useState('');
  const [largeur, setLargeur] = useState('');
  const [tirantEau, setTirantEau] = useState('');
  const [armateur, setArmateur] = useState('');
  const [saving, setSaving] = useState(false);

  // Filtered ships
  const filteredNavires = useMemo(() => {
    return navires.filter(n => {
      const matchSearch = (n.nom || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
                          (n.armateur || '').toLowerCase().includes(searchTerm.toLowerCase());
      const matchType = typeFilter === 'ALL' || n.typeNavire === typeFilter;
      return matchSearch && matchType;
    });
  }, [navires, searchTerm, typeFilter]);

  const openAddModal = () => {
    setEditingNavire(null);
    setNom('');
    setTjb('');
    setTypeNavire('AUTRE');
    setLongueur('');
    setLargeur('');
    setTirantEau('');
    setArmateur('');
    setIsModalOpen(true);
  };

  const openEditModal = (navire) => {
    setEditingNavire(navire);
    setNom(navire.nom || '');
    setTjb(navire.tjb || '');
    setTypeNavire(navire.typeNavire || 'AUTRE');
    setLongueur(navire.longueur || '');
    setLargeur(navire.largeur || '');
    setTirantEau(navire.tirantEau || '');
    setArmateur(navire.armateur || '');
    setIsModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);

    const payload = {
      nom,
      tjb: parseFloat(tjb),
      typeNavire,
      longueur: longueur ? parseFloat(longueur) : null,
      largeur: largeur ? parseFloat(largeur) : null,
      tirantEau: tirantEau ? parseFloat(tirantEau) : null,
      armateur
    };

    if (editingNavire) {
      await api.updateNavire(editingNavire.idNavire, payload);
    } else {
      await api.createNavire(payload);
    }

    setSaving(false);
    setIsModalOpen(false);
    onRefresh();
  };

  const handleDelete = async (id) => {
    if (window.confirm('Voulez-vous vraiment supprimer ce navire ?')) {
      await api.deleteNavire(id);
      onRefresh();
    }
  };

  return (
    <div className="ships-page">
      {/* Top Header Bar */}
      <div className="page-header card">
        <div className="page-title">
          <div className="title-icon bg-navy">
            <Ship size={28} color="#FFFFFF" />
          </div>
          <div>
            <h2>Tous les Navires</h2>
            <p>Registre officiel des navires de commerce et de passage enregistrés auprès de l'ANP</p>
          </div>
        </div>
        <button className="btn btn-primary" onClick={openAddModal}>
          <Plus size={18} /> Nouveau Navire
        </button>
      </div>

      {/* Filter & Search Bar */}
      <div className="filter-bar card">
        <div className="search-input-box">
          <Search size={18} color="#64748B" />
          <input
            type="text"
            placeholder="Rechercher par nom de navire ou armateur..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          {searchTerm && (
            <button className="clear-btn" onClick={() => setSearchTerm('')}>
              <X size={16} />
            </button>
          )}
        </div>

        <div className="filter-group">
          <Filter size={18} color="#64748B" />
          <select value={typeFilter} onChange={(e) => setTypeFilter(e.target.value)} className="form-control" style={{ width: '220px' }}>
            <option value="ALL">Tous les Types de Navires</option>
            <option value="TRANSBORDEUR">Transbordeur (car-ferry)</option>
            <option value="AUTRE">Autre navire</option>
          </select>
        </div>
      </div>

      {/* Data Table */}
      <div className="table-responsive card">
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nom du Navire</th>
              <th>Type de Navire</th>
              <th>Tonnage (TJB)</th>
              <th>Dimensions (L x l x TE)</th>
              <th>Armateur / Compagnie</th>
              <th style={{ textAlign: 'right' }}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredNavires.length > 0 ? (
              filteredNavires.map((n) => (
                <tr key={n.idNavire}>
                  <td><strong>#{n.idNavire}</strong></td>
                  <td>
                    <strong style={{ color: 'var(--anp-navy)', fontSize: '0.95rem' }}>{n.nom}</strong>
                  </td>
                  <td>
                    <span className={`badge ${n.typeNavire === 'TRANSBORDEUR' ? 'badge-gold' : 'badge-navy'}`}>
                      {n.typeNavire === 'TRANSBORDEUR' ? 'Transbordeur' : 'Autre navire'}
                    </span>
                  </td>
                  <td><strong>{n.tjb?.toLocaleString('fr-FR')} T</strong></td>
                  <td>
                    {n.longueur || '-'}m x {n.largeur || '-'}m (TE: {n.tirantEau || '-'}m)
                  </td>
                  <td>{n.armateur || 'Non spécifié'}</td>
                  <td style={{ textAlign: 'right' }}>
                    <div className="action-btns">
                      <button className="btn btn-outline btn-sm" onClick={() => openEditModal(n)}>
                        <Edit size={14} /> Éditer
                      </button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(n.idNavire)}>
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center', padding: '3rem 1rem', color: '#64748B' }}>
                  Aucun navire ne correspond aux critères de recherche.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Modal Add/Edit */}
      {isModalOpen && (
        <div className="modal-overlay" onClick={() => setIsModalOpen(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>
                <Ship size={20} color="#D4AF37" /> {editingNavire ? 'Modifier le Navire' : 'Ajouter un Nouveau Navire'}
              </h3>
              <button className="modal-close" onClick={() => setIsModalOpen(false)}>
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <label>Nom du Navire *</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Ex: TARIQ IBN ZIYAD"
                    value={nom}
                    onChange={(e) => setNom(e.target.value)}
                    required
                  />
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>Tonnage de Jauge Brute (TJB) *</label>
                    <input
                      type="number"
                      step="0.01"
                      className="form-control"
                      placeholder="Ex: 14500"
                      value={tjb}
                      onChange={(e) => setTjb(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Type de Navire *</label>
                    <select
                      className="form-control"
                      value={typeNavire}
                      onChange={(e) => setTypeNavire(e.target.value)}
                      required
                    >
                      <option value="TRANSBORDEUR">Transbordeur (car-ferry)</option>
                      <option value="AUTRE">Autre navire</option>
                    </select>
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>Longueur Hors Tout (m)</label>
                    <input
                      type="number"
                      step="0.1"
                      className="form-control"
                      placeholder="Ex: 155"
                      value={longueur}
                      onChange={(e) => setLongueur(e.target.value)}
                    />
                  </div>

                  <div className="form-group">
                    <label>Largeur / Bau (m)</label>
                    <input
                      type="number"
                      step="0.1"
                      className="form-control"
                      placeholder="Ex: 24"
                      value={largeur}
                      onChange={(e) => setLargeur(e.target.value)}
                    />
                  </div>

                  <div className="form-group">
                    <label>Tirant d'Eau Max (m)</label>
                    <input
                      type="number"
                      step="0.1"
                      className="form-control"
                      placeholder="Ex: 6.5"
                      value={tirantEau}
                      onChange={(e) => setTirantEau(e.target.value)}
                    />
                  </div>
                </div>

                <div className="form-group">
                  <label>Armateur / Compagnie Maritime</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Ex: Comanav / CMA CGM"
                    value={armateur}
                    onChange={(e) => setArmateur(e.target.value)}
                  />
                </div>
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setIsModalOpen(false)}>
                  Annuler
                </button>
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  <Check size={16} /> {saving ? 'Enregistrement...' : 'Enregistrer le Navire'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <style>{`
        .ships-page {
          display: flex;
          flex-direction: column;
          gap: 1.5rem;
        }

        .filter-bar {
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: 1.5rem;
          padding: 1rem 1.5rem;
          flex-wrap: wrap;
        }

        .search-input-box {
          display: flex;
          align-items: center;
          gap: 0.6rem;
          background: #F8FAFC;
          border: 1px solid var(--anp-border);
          border-radius: var(--radius-sm);
          padding: 0.5rem 0.85rem;
          flex: 1;
          min-width: 280px;
        }

        .search-input-box input {
          border: none;
          background: transparent;
          width: 100%;
          outline: none;
          font-size: 0.9rem;
        }

        .clear-btn {
          background: transparent;
          border: none;
          cursor: pointer;
          color: #94A3B8;
        }

        .filter-group {
          display: flex;
          align-items: center;
          gap: 0.6rem;
        }

        .action-btns {
          display: flex;
          align-items: center;
          justify-content: flex-end;
          gap: 0.5rem;
        }
      `}</style>
    </div>
  );
}
